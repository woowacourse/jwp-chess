package chess.domain.pieces;

import chess.domain.pieces.exceptions.CanNotMoveException;
import chess.domain.pieces.exceptions.CanNotReachException;
import chess.domain.coordinate.Direction;
import chess.domain.coordinate.Distance;
import chess.domain.coordinate.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static chess.domain.testAssistant.creationAssistant.createDirection;
import static chess.domain.testAssistant.creationAssistant.createDistance;
import static chess.domain.testAssistant.creationAssistant.createKing;
import static chess.domain.testAssistant.creationAssistant.createPoint;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KingTest {
	King king;

	@BeforeEach
	void setUp() {
		king = createKing("black", "a1");
	}

	@Test
	void move() {
		Coordinate coordinate = createPoint("a2");
		King expect = createKing("black", "a2");

		assertThat(king.move(coordinate)).isEqualTo(expect);
	}

	@Test
	void canMove_ThrowException() {
		Direction direction = createDirection("knight");

		assertThatThrownBy(() -> king.validateMoveDirection(direction))
				.isInstanceOf(CanNotMoveException.class);
	}

	@Test
	void canReach() {
		Distance distance = createDistance("vertical_two");

		assertThatThrownBy(() -> king.validateReach(distance))
				.isInstanceOf(CanNotReachException.class);

	}
}