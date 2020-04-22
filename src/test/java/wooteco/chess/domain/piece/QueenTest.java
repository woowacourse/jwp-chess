package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.position.Fixtures.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueenTest {
	Queen queen;

	@BeforeEach
	void setUp() {
		queen = new Queen(C3, Team.BLACK);
	}

	@Test
	void canNotMoveTo_Return_True_When_OutOfMovableArea() {
		Piece target = new Empty(D5);
		assertThat(queen.canNotMoveTo(target)).isTrue();
	}

	@Test
	void canNotMoveTo_Return_False_When_InMovableArea() {
		Piece target = new Empty(C4);
		assertThat(queen.canNotMoveTo(target)).isFalse();
	}
}