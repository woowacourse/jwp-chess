package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.ChessGameRoom;
import chess.domain.state.Turn;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessGameDaoTest {

    private ChessGameDao chessGameDao;
    private ChessGameRoom chessGameRoom;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
        chessGameRoom = new ChessGameRoom("title", "password");
    }

    @Test
    @DisplayName("체스 게임 생성")
    void createChessGame() {
        assertDoesNotThrow(() -> chessGameDao.createChessGame(chessGameRoom));
    }

    @Test
    @DisplayName("체스게임 목록 반환")
    void findAllChessGame() {
        chessGameDao.createChessGame(chessGameRoom);
        List<ChessGameRoom> chessGameRooms = chessGameDao.findAllChessGames();
        assertThat(chessGameRooms).hasSize(1);
    }

    @Test
    @DisplayName("현재 게임 상태 반환")
    void findChessGame() {
        long id = chessGameDao.createChessGame(chessGameRoom);

        assertThat(chessGameDao.findChessGame(id)).isEqualTo(Turn.WHITE_TURN);
    }

    @Test
    @DisplayName("현재 게임 상태 변경")
    void changeChessGameTurn() {
        long id = chessGameDao.createChessGame(chessGameRoom);

        assertThat(chessGameDao.changeChessGameTurn(id, Turn.END)).isEqualTo(1);
    }
}
