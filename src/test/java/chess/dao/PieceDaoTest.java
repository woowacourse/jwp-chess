package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.single.King;
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
		Map<Position, Piece> pieces = Map.of(Position.of('a', '1'), new Piece(Color.WHITE, new King()),
				Position.of('a', '2'), new Piece(Color.BLACK, new King()));

		assertThat(pieceDao.savePieces(pieces)).isEqualTo(2);
	}

	@Test
	@DisplayName("전체 피스 조회")
	void findAllPieces() {
		pieceDao.savePieces(Map.of(Position.of('a', '1'), new Piece(Color.WHITE, new King()),
				Position.of('a', '2'), new Piece(Color.BLACK, new King())));

		Map<Position, Piece> pieces = pieceDao.findAllPieces();
		assertThat(pieces).hasSize(2);
		System.out.println();
	}

	@Test
	@DisplayName("피스 위치 업데이트")
	void updatePiecePosition() {
		Position source = Position.from("a1");
		Position target = Position.from("a2");
		pieceDao.savePieces(Map.of(source, new Piece(Color.WHITE, new King())));

		assertThat(pieceDao.updatePiece(source, target)).isEqualTo(1);
	}
}
