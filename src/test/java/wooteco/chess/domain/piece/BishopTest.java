package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.position.Fixtures.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BishopTest {
	Bishop bishop;

	@BeforeEach
	void setUp() {
		bishop = new Bishop(C3, Team.BLACK);
	}

	@Test
	void canNotMoveTo_Return_True_When_OutOfMovableArea() {
		Piece target = new Empty(C4);
		assertThat(bishop.canNotMoveTo(target)).isTrue();
	}

	@Test
	void canNotMoveTo_Return_False_When_InMovableArea() {
		Piece target = new Empty(B4);
		assertThat(bishop.canNotMoveTo(target)).isFalse();
	}
}