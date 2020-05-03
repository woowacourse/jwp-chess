package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.position.Fixtures.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KnightTest {
	Knight knight;

	@BeforeEach
	void setUp() {
		knight = new Knight(C3, Turn.BLACK);
	}

	@Test
	void canNotMoveTo_Return_True_When_OutOfMovableArea() {
		Piece target = new Empty(B3);
		assertThat(knight.canNotMoveTo(target)).isTrue();
	}

	@Test
	void canNotMoveTo_Return_False_When_InMovableArea() {
		Piece target = new Empty(A2);
		assertThat(knight.canNotMoveTo(target)).isFalse();
	}
}
