package chess.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import chess.dto.ChessGameDto;

@JdbcTest
@Sql({"/db/schema.sql"})
class ChessGameDaoTest {

    private ChessGameDao chessGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
    }

    @DisplayName("체스 게임방 데이터를 삽입한다.")
    @Test
    void insert() {
        ChessGameDto chessGameDto = new ChessGameDto("제목2", "password");

        chessGameDao.insert(chessGameDto);

        assertThat(chessGameDao.find(2L).getTitle()).isEqualTo("제목2");
    }

    @DisplayName("체스 게임 방 데이터를 삭제한다.")
    @Test
    void delete() {
        assertThat(chessGameDao.delete(1L)).isEqualTo(1);
    }

    @DisplayName("체스 게임 방 데이터를 모두 가져온다.")
    @Test
    void findAll() {
        assertThat(chessGameDao.findAll().size()).isEqualTo(1);
    }
}
