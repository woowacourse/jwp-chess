package chess.dao;

import static chess.domain.state.Turn.END;
import static chess.domain.state.Turn.WHITE_TURN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.ChessGame;
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
        ChessGame chessGame = new ChessGame(WHITE_TURN.name(), "title", "password");

        assertDoesNotThrow(() -> chessGameDao.createChessGame(chessGame));
    }

    @Test
    @DisplayName("중복된 제목의 체스 게임 생성")
    void createDuplicateTitleChessGame() {
        ChessGame chessGame = new ChessGame(WHITE_TURN.name(), "title", "password");
        chessGameDao.createChessGame(chessGame);

        assertThatThrownBy(() -> chessGameDao.createChessGame(chessGame))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("id 값을 통해서 체스 게임 반환")
    void findChessGame() {
        ChessGame chessGame = new ChessGame(WHITE_TURN.name(), "title", "password");
        ChessGame savedChessGame = chessGameDao.createChessGame(chessGame);

        assertThat(chessGameDao.findChessGame(savedChessGame.getId())).isEqualTo(savedChessGame);
    }

    @Test
    @DisplayName("현재 게임 상태 변경")
    void changeChessGameTurn() {
        ChessGame chessGame = new ChessGame(WHITE_TURN.name(), "title", "password");
        ChessGame savedChessGame = chessGameDao.createChessGame(chessGame);

        assertThat(chessGameDao.changeChessGameTurn(savedChessGame.getId(), END)).isEqualTo(1);
    }

    @Test
    @DisplayName("체스 게임 제목으로 게임 삭제")
    void deleteChessGameByTitle() {
        ChessGame chessGame = new ChessGame(WHITE_TURN.name(), "title", "password");
        ChessGame savedChessGame = chessGameDao.createChessGame(chessGame);

        assertThat(chessGameDao.deleteChessGame(savedChessGame)).isEqualTo(1);
    }
}
