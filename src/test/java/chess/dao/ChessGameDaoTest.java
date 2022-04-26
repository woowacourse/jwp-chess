package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.ChessGame;
import chess.domain.state.Turn;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessGameDaoTest {

    private ChessGameDao chessGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setup() {
        chessGameDao = new ChessGameDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("체스 게임 생성")
    void createChessGame() {
        Turn turn = Turn.WHITE_TURN;
        ChessGame chessGame = new ChessGame(turn.name(), "title", "password");

        assertDoesNotThrow(() -> chessGameDao.createChessGame(chessGame));
    }

    @Test
    @DisplayName("현재 게임 상태 반환")
    void findChessGame() {
        Turn turn = Turn.WHITE_TURN;
        ChessGame chessGame = new ChessGame(turn.name(), "title", "password");
        ChessGame savedChessGame = chessGameDao.createChessGame(chessGame);

        assertThat(chessGameDao.findChessGame(savedChessGame.getId())).isEqualTo(turn);
    }

    @Test
    @DisplayName("현재 게임 상태 변경")
    void changeChessGameTurn() {
        Turn turn = Turn.WHITE_TURN;
        ChessGame chessGame = new ChessGame(turn.name(), "title", "password");
        ChessGame savedChessGame = chessGameDao.createChessGame(chessGame);

        assertThat(chessGameDao.changeChessGameTurn(savedChessGame.getId(), Turn.END)).isEqualTo(1);
    }
}
