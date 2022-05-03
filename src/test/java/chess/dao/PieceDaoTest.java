package chess.dao;

import static chess.domain.piece.Color.*;
import static chess.domain.position.File.A;
import static chess.domain.position.File.B;
import static chess.domain.position.File.C;
import static chess.domain.position.File.D;
import static chess.domain.position.Rank.EIGHT;
import static chess.domain.position.Rank.SEVEN;
import static chess.domain.position.Rank.SIX;
import static org.assertj.core.api.Assertions.assertThat;
import chess.domain.ChessBoardInitializer;
import chess.domain.ChessGame;
import chess.domain.GameScore;
import chess.domain.Score;
import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.position.Position;
import chess.domain.vo.Room;
import chess.dto.GameStatus;
import chess.dto.PieceDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class PieceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PieceDao pieceDao;
    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
        chessGameDao.saveChessGame(
            new ChessGame(
                new Room("Chess Game", "1234"),
                GameStatus.RUNNING,
                WHITE,
                new GameScore(new Score(BigDecimal.ONE), new Score(BigDecimal.ONE))
            )
        );

        pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.deleteById(getChessGameId());
        Map<Position, Piece> initBoard = ChessBoardInitializer.getInitBoard();
        initBoard.forEach((key, value) -> pieceDao.savePiece(getChessGameId(), key, value));
    }

    @AfterEach
    void tearDown() {
        pieceDao.deleteById(getChessGameId());
    }

    @Test
    void findPieces() {
        assertThat(pieceDao.findPieces(getChessGameId())).hasSize(32);
    }

    @Test
    void deletePieceByPosition() {
        pieceDao.deletePieceByPosition(getChessGameId(), new Position(A, SEVEN));
        pieceDao.deletePieceByPosition(getChessGameId(), new Position(B, SEVEN));
        assertThat(pieceDao.findPieces(getChessGameId())).hasSize(30);
    }

    private int getChessGameId() {
        return chessGameDao.findAll().get(0).getId();
    }
}
