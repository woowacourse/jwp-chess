package chess.dao;

import static chess.ChessGameFixture.createRunningChessGame;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.pawn.Pawn;
import chess.domain.piece.single.King;
import chess.domain.piece.single.Knight;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PieceDaoTest {

    private PieceDao pieceDao;
    private ChessGameDao chessGameDao;
    private long chessGameId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    void setUpDao(){
        pieceDao = new PieceDao(jdbcTemplate);
        chessGameDao = new ChessGameDao(jdbcTemplate, dataSource);
    }

    @BeforeEach
    void setUp() {
        ChessGame chessGame = createRunningChessGame();
        ChessGame savedChessGame = chessGameDao.createChessGame(chessGame);
        chessGameId = savedChessGame.getId();
    }

    @Test
    @DisplayName("체스 기물 정보 저장")
    void savePieces() {
        Map<Position, Piece> pieces = Map.of(Position.of('a', '1'), new Piece(Color.WHITE, new King()),
                Position.of('a', '2'), new Piece(Color.BLACK, new King()));

        assertThat(pieceDao.savePieces(chessGameId, pieces)).isEqualTo(2);
    }

    @Test
    @DisplayName("전체 피스 조회")
    void findAllPieces() {
        pieceDao.savePieces(chessGameId, Map.of(Position.of('a', '1'), new Piece(Color.WHITE, new King()),
                Position.of('a', '2'), new Piece(Color.BLACK, new King())));
        ChessBoard chessBoard = pieceDao.findChessBoardByChessGameId(chessGameId);

        assertThat(chessBoard.getPieces()).hasSize(2);
    }

    @Test
    @DisplayName("피스 위치 업데이트")
    void updatePiecePosition() {
        Position source = Position.from("a1");
        Position target = Position.from("a2");
        pieceDao.savePieces(chessGameId, Map.of(source, new Piece(Color.WHITE, new King())));

        assertThat(pieceDao.updatePiecePosition(chessGameId, source, target)).isEqualTo(1);
    }

    @Test
    @DisplayName("피스 이동 규칙 업데이트")
    void updatePieceRule() {
        Position source = Position.from("a1");
        pieceDao.savePieces(chessGameId, Map.of(source, new Piece(Color.WHITE, new Pawn(Color.WHITE))));

        assertThat(pieceDao.updatePieceRule(chessGameId, source, new Knight())).isEqualTo(1);
    }

    @Test
    @DisplayName("피스 삭제")
    void deletePiece() {
        Position source = Position.from("a1");
        pieceDao.savePieces(chessGameId, Map.of(source, new Piece(Color.WHITE, new Pawn(Color.WHITE))));

        assertThat(pieceDao.delete(chessGameId, source)).isEqualTo(1);
    }
}
