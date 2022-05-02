package chess.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.db.ChessGameDao;
import chess.db.PieceDao;
import chess.domain.game.ChessGame;
import chess.domain.game.GameTurn;
import chess.domain.board.InitialBoardGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class ChessServiceTest {

    private ChessService chessService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initTable() {
        ChessGameDao chessGameDao = new ChessGameDao(jdbcTemplate);
        chessGameDao.save("test", "test", new ChessGame(new InitialBoardGenerator(), GameTurn.WHITE));

        PieceDao pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.save("test");

        chessService = new ChessService(new ChessGameDao(jdbcTemplate), new PieceDao(jdbcTemplate));
    }

    @AfterEach
    void deleteTable() {
        String deletePieceSql = "delete from piece where gameID = 'test'";
        jdbcTemplate.update(deletePieceSql);

        String deleteChessGameSql = "delete from chessGame where gameID = 'test'";
        jdbcTemplate.update(deleteChessGameSql);
    }

    @Test
    @DisplayName("gameID를 이용해 DB로부터 불러온 turn이 해당 gameTurn과 일치한다")
    void getTurn() {
        assertThat(chessService.getTurn("test")).isEqualTo(GameTurn.WHITE);
    }

    @Test
    @DisplayName("gameID를 이용해 DB로부터 불러온 chessGame의 종료여부가 실제 종료여부 일치한다")
    void isFinished() {
        assertThat(chessService.isFinished("test")).isEqualTo(false);
    }

    @Test
    @DisplayName("gameCode를 이용해 DB로부터 불러온 gameID가 실제 gameID와 일치한다")
    void findGameID() {
        assertThat(chessService.findGameID("hashtesttestval")).isEqualTo("test");
    }
}
