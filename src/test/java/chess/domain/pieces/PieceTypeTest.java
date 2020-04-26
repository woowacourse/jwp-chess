package chess.domain.pieces;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PieceTypeTest {

	@Test
	void ofName() {
		assertThat(PieceType.ofName("pawn")).isEqualTo(PieceType.PAWN);
	}
}