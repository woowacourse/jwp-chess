package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dto.BoardDto;
import chess.dto.GameDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardJdbcDaoTest {

    @Autowired
    private BoardDao boardDao;
    @Autowired
    private GameDao gameDao;

    private List<BoardDto> boardDto = new ArrayList<>();
    private int gameId;

    @BeforeEach
    void init() {
        boardDto.add(new BoardDto("PAWN", "WHITE", "a2"));
        gameId = gameDao.save(new GameDto("a", "b", "WHITE"));
        boardDao.save(boardDto, gameId);
    }

    @AfterEach
    void clear() {
        boardDto.clear();
        gameDao.deleteById(gameId);
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
