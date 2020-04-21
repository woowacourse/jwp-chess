package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.position.Direction;
import wooteco.chess.domain.piece.position.InvalidPositionException;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;

public class King extends Piece {
	private static final String SYMBOL = "k";
	private static final int MAX_STEP_SIZE = 1;
	private static final double SCORE = 0;

	public King(Position position, Team team) {
		super(position, team);
	}

	@Override
	protected void validateDirection(Direction direction) {
		boolean isWrongDirection = !Direction.isEveryDirection(direction);
		if (isWrongDirection) {
			throw new InvalidPositionException(InvalidPositionException.INVALID_DIRECTION);
		}
	}

	@Override
	protected void validateStepSize(Position sourcePosition, Position targetPosition) {
		int rowGap = this.position.calculateRowGap(targetPosition);
		int columnGap = this.position.calculateColumnGap(targetPosition);
		boolean isWrongStepSize = (Math.abs(rowGap) > MAX_STEP_SIZE) || (Math.abs(columnGap) > MAX_STEP_SIZE);
		if (isWrongStepSize) {
			throw new InvalidPositionException(InvalidPositionException.INVALID_STEP_SIZE);
		}
	}

	@Override
	protected void validateRoute(Direction direction, Position targetPosition, Board board) {
	}

	@Override
	protected String getSymbol() {
		return SYMBOL;
	}

	@Override
	public double getScore() {
		return SCORE;
	}
}
