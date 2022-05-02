package chess.db;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.domain.game.ChessGame;
import chess.domain.game.GameTurn;
import chess.domain.board.InitialBoardGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class PieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initTable() {
        ChessGameDao chessGameDao = new ChessGameDao(jdbcTemplate);
        chessGameDao.save("test", "test", new ChessGame(new InitialBoardGenerator(), GameTurn.WHITE));
    }

    @AfterEach
    void deleteTable() {
        String deletePieceSql = "delete from piece where gameID = 'test'";
        jdbcTemplate.update(deletePieceSql);

        String deleteChessGameSql = "delete from chessGame where gameID = 'test'";
        jdbcTemplate.update(deleteChessGameSql);
    }

    @DisplayName("존재하는 게임에 대한 검색은 예외를 반환하지 않는다")
    @Test
    void findByGameID() {
        PieceDao pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.save("test");

        assertThatNoException().isThrownBy(() -> pieceDao.findByGameID("test"));
    }

    @DisplayName("존재하지 않는 게임에 대한 검색은 예외를 반환한다")
    @Test
    void findByGameID_NoSuchGame() {
        PieceDao pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.save("test");

        assertThatThrownBy(() -> pieceDao.findByGameID("test1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("헉.. 저장 안한거 아냐? 그런 게임은 없어!");
    }
}
