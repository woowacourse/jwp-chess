package wooteco.chess.domain.piece;

import static wooteco.chess.domain.position.Direction.*;

import java.util.Arrays;

public class Knight extends Piece {
	private static final double KNIGHT_SCORE = 2.5;

	public Knight(Color color) {
		super(color, "n", new FixedMovingStrategy(Arrays.asList(
			LEFT_LEFT_DOWN, LEFT_LEFT_UP,
			RIGHT_RIGHT_DOWN, RIGHT_RIGHT_UP,
			LEFT_DOWN_DOWN, LEFT_UP_UP,
			RIGHT_DOWN_DOWN, RIGHT_UP_UP)),
			KNIGHT_SCORE);
	}

	@Override
	public boolean isKing() {
		return false;
	}
}
