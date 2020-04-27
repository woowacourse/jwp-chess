package chess.domain.pieces;

import chess.domain.pieces.exceptions.CanNotMoveException;
import chess.domain.coordinate.Direction;
import chess.domain.coordinate.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static chess.domain.testAssistant.creationAssistant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KnightTest {
	Knight knight;

	@BeforeEach
	void setUp() {
		knight = createKnight("black", "a1");
	}

	@Test
	void move() {
		Coordinate coordinate = createPoint("b3");
		Knight expect = createKnight("black", "b3");

		assertThat(knight.move(coordinate)).isEqualTo(expect);
	}

	@Test
	void canMove() {
		Direction direction = createDirection("North");

		assertThatThrownBy(() -> knight.validateMoveDirection(direction))
				.isInstanceOf(CanNotMoveException.class);
	}
}