package chess.domain.dao;

import chess.dao.SpringBoardDao;
import chess.domain.Side;
import chess.domain.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@JdbcTest
@Transactional
public class SpringBoardDaoTest {
    private static final String existRoomName = "testRoomName";

    private SpringBoardDao springBoardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springBoardDao = new SpringBoardDao(jdbcTemplate);
        jdbcTemplate.execute("set mode MySQL");
    }

    @Test
    void initBoardTest() {
        assertThat(springBoardDao.initBoard(existRoomName))
                .isEqualTo(Board.getGamingBoard());
    }

    @Test
    void newBoardTest() {
        assertThat(springBoardDao.newBoard("newRoomName"))
                .isEqualTo(Board.getGamingBoard().getBoard());
    }

    @Test
    void findBoardTest() {
        assertThat(springBoardDao.findBoard(existRoomName).get())
                .isEqualTo(Board.getGamingBoard());
    }

    @Test
    void findTurnTest() {
        assertThat(springBoardDao.findTurn(existRoomName).get())
                .isEqualTo(Side.WHITE);
    }
}
