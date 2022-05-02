package chess.db;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
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
public class ChessGameDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void deleteTable() {
        String deletePieceSql = "delete from piece where gameID = 'test'";
        jdbcTemplate.update(deletePieceSql);

        String deleteChessGameSql = "delete from chessGame where gameID = 'test'";
        jdbcTemplate.update(deleteChessGameSql);
    }

    @DisplayName("존재하는 게임에 대한 검색은 예외를 반환하지 않는다")
    @Test
    void findTurnByID() {
        ChessGameDao chessGameDao = new ChessGameDao(jdbcTemplate);
        ChessGame chessGame = new ChessGame(new InitialBoardGenerator(), GameTurn.BLACK);
        chessGameDao.save("test", "test", chessGame);

        assertThat(chessGameDao.findTurnByID("test")).isEqualTo("BLACK");
    }
}
