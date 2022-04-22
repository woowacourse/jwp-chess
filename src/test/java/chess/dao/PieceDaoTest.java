package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.ChessBoard;
import chess.domain.Color;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.pawn.Pawn;
import chess.domain.piece.single.King;
import chess.domain.piece.single.Knight;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class PieceDaoTest {

	private PieceDao pieceDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() {
		pieceDao = new PieceDao(jdbcTemplate);
	}

	@Test
	void savePieces() {
		Map<Position, Piece> pieces = Map.of(Position.of('a', '1'), new Piece(1L, Color.WHITE, new King()),
				Position.of('a', '2'), new Piece(1L, Color.BLACK, new King()));

		assertThat(pieceDao.savePieces(1L, pieces)).isEqualTo(2);
	}

	@Test
	@DisplayName("전체 피스 조회")
	void findAllPieces() {
		pieceDao.savePieces(1L, Map.of(Position.of('a', '1'), new Piece(1L, Color.WHITE, new King()),
				Position.of('a', '2'), new Piece(1L, Color.BLACK, new King())));

		ChessBoard chessBoard = pieceDao.findChessBoardByChessGameId(1L);
		assertThat(chessBoard.getPieces()).hasSize(2);
	}

	@Test
	@DisplayName("피스 위치 업데이트")
	void updatePiecePosition() {
		Position source = Position.from("a1");
		Position target = Position.from("a2");
		pieceDao.savePieces(1L, Map.of(source, new Piece(1L, Color.WHITE, new King())));

		assertThat(pieceDao.updatePiecePosition(source, target)).isEqualTo(1);
	}

	@Test
	@DisplayName("피스 이동 규칙 업데이트")
	void updatePieceRule() {
		Position source = Position.from("a1");
		pieceDao.savePieces(1L, Map.of(source, new Piece(1L, Color.WHITE, new Pawn(Color.WHITE))));

		assertThat(pieceDao.updatePieceRule(source, new Knight())).isEqualTo(1);
	}

	@Test
	@DisplayName("피스 삭제")
	void deletePiece() {
		Position source = Position.from("a1");
		pieceDao.savePieces(1L, Map.of(source, new Piece(1L, Color.WHITE, new Pawn(Color.WHITE))));

		assertThat(pieceDao.delete(source)).isEqualTo(1);
	}
}
