package chess.domain.pieces;

import chess.domain.pieces.exceptions.CanNotMoveException;
import chess.domain.coordinate.Direction;
import chess.domain.coordinate.Coordinate;
import org.junit.jupiter.api.Test;

import static chess.domain.testAssistant.creationAssistant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QueenTest {
	Queen queen = createQueen("black", "a1");

	@Test
	void move() {
		Coordinate coordinate = createPoint("a2");
		Queen expect = createQueen("black", "a2");

		assertThat(queen.move(coordinate)).isEqualTo(expect);
	}

	@Test
	void canMove() {
		Direction direction = createDirection("knight");

		assertThatThrownBy(() -> queen.validateMoveDirection(direction))
				.isInstanceOf(CanNotMoveException.class);
	}
}