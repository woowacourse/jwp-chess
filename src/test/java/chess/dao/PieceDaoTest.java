package chess.dao;

import static chess.domain.position.File.A;
import static chess.domain.position.File.B;
import static chess.domain.position.File.C;
import static chess.domain.position.File.D;
import static chess.domain.position.Rank.EIGHT;
import static chess.domain.position.Rank.SEVEN;
import static chess.domain.position.Rank.SIX;
import static org.assertj.core.api.Assertions.assertThat;
import chess.domain.Score;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.vo.Room;
import chess.dto.GameStatus;
import chess.dto.PieceDto;
import chess.dto.PieceType;
import java.math.BigDecimal;
import java.util.List;
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
        chessGameDao.saveChessGame(new Room("Chess Game", "1234"), GameStatus.RUNNING, Color.WHITE,
                new Score(BigDecimal.ONE), new Score(BigDecimal.ONE));

        pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.deleteAll(getChessGameId());
        pieceDao.savePieces(getChessGameId(), List.of(
                new PieceDto(new Position(A, EIGHT), PieceType.KING, Color.WHITE),
                new PieceDto(new Position(A, SEVEN), PieceType.QUEEN, Color.WHITE),
                new PieceDto(new Position(B, EIGHT), PieceType.PAWN, Color.WHITE),
                new PieceDto(new Position(C, EIGHT), PieceType.BISHOP, Color.WHITE),
                new PieceDto(new Position(D, EIGHT), PieceType.KING, Color.BLACK),
                new PieceDto(new Position(D, SEVEN), PieceType.KNIGHT, Color.BLACK),
                new PieceDto(new Position(D, SIX), PieceType.PAWN, Color.BLACK)
        ));
    }

    private int getChessGameId() {
        return chessGameDao.findAll().get(0).getId();
    }

    @AfterEach
    void tearDown() {
        pieceDao.deleteAll(getChessGameId());
    }

    @Test
    void findPieces() {
        assertThat(pieceDao.findPieces(getChessGameId())).containsExactlyInAnyOrder(
                new PieceDto(new Position(A, EIGHT), PieceType.KING, Color.WHITE),
                new PieceDto(new Position(A, SEVEN), PieceType.QUEEN, Color.WHITE),
                new PieceDto(new Position(B, EIGHT), PieceType.PAWN, Color.WHITE),
                new PieceDto(new Position(C, EIGHT), PieceType.BISHOP, Color.WHITE),
                new PieceDto(new Position(D, EIGHT), PieceType.KING, Color.BLACK),
                new PieceDto(new Position(D, SEVEN), PieceType.KNIGHT, Color.BLACK),
                new PieceDto(new Position(D, SIX), PieceType.PAWN, Color.BLACK)
        );
    }

    @Test
    void deletePieceByPosition() {
        pieceDao.deletePieceByPosition(getChessGameId(), new Position(A, SEVEN));
        pieceDao.deletePieceByPosition(getChessGameId(), new Position(D, SIX));
        assertThat(pieceDao.findPieces(getChessGameId())).containsExactlyInAnyOrder(
                new PieceDto(new Position(A, EIGHT), PieceType.KING, Color.WHITE),
                new PieceDto(new Position(B, EIGHT), PieceType.PAWN, Color.WHITE),
                new PieceDto(new Position(C, EIGHT), PieceType.BISHOP, Color.WHITE),
                new PieceDto(new Position(D, EIGHT), PieceType.KING, Color.BLACK),
                new PieceDto(new Position(D, SEVEN), PieceType.KNIGHT, Color.BLACK)
        );
    }
}
