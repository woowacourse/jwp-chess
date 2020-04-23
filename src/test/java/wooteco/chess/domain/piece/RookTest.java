package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.position.Fixtures.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RookTest {
	Rook rook;

	@BeforeEach
	void setUp() {
		rook = new Rook(C3, Team.BLACK);
	}

	@Test
	void canNotMoveTo_Return_True_When_OutOfMovableArea() {
		Piece target = new Empty(D5);
		assertThat(rook.canNotMoveTo(target)).isTrue();
	}

	@Test
	void canNotMoveTo_Return_False_When_InMovableArea() {
		Piece target = new Empty(C4);
		assertThat(rook.canNotMoveTo(target)).isFalse();
	}
}