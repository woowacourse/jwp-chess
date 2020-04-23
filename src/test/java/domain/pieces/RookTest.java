package domain.pieces;

import domain.pieces.exceptions.CanNotMoveException;
import domain.coordinate.Direction;
import domain.coordinate.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.testAssistant.creationAssistant.createDirection;
import static domain.testAssistant.creationAssistant.createPoint;
import static domain.testAssistant.creationAssistant.createRook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RookTest {
	Rook rook;

	@BeforeEach
	void setUp() {
		rook = createRook("black", "a1");
	}

	@Test
	void move() {
		Coordinate coordinate = createPoint("a2");
		Rook expect = createRook("black", "a2");

		assertThat(rook.move(coordinate)).isEqualTo(expect);
	}

	@Test
	void canMove() {
		Direction direction = createDirection("North_East");

		assertThatThrownBy(() -> rook.validateMoveDirection(direction))
				.isInstanceOf(CanNotMoveException.class);
	}
}