package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dto.BoardDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql(value = {"../../../resources/schema.sql"})
class BoardJdbcDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BoardDao boardDao;
    private int gameId = 1;


    @BeforeEach
    void setup() {
        boardDao = new BoardJdbcDao(jdbcTemplate);
    }

    @Test
    void findByGameId() {
        List<BoardDto> boardDtos = boardDao.findByGameId(gameId);
        assertThat(boardDtos.get(0).getSymbol()).isEqualTo("PAWN");
    }

    @Test
    void update() {
        BoardDto boardDto = new BoardDto("BISHOP", "WHITE", "a2");
        boardDao.update(boardDto, gameId);

        List<BoardDto> boardDtos = boardDao.findByGameId(gameId);
        assertThat(boardDtos.get(0).getSymbol()).isEqualTo("BISHOP");
    }
}
