package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.position.Direction;
import wooteco.chess.domain.piece.position.InvalidPositionException;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;

public class Knight extends Piece {
	private static final String SYMBOL = "n";
	private static final double SCORE = 2.5;

	public Knight(Position position, Team team) {
		super(position, team);
	}

	@Override
	protected void validateDirection(Direction direction) {
		boolean isWrongDirection = !Direction.isKnightDirection(direction);
		if (isWrongDirection) {
			throw new InvalidPositionException(InvalidPositionException.INVALID_DIRECTION);
		}
	}

	@Override
	protected void validateStepSize(Position sourcePosition, Position targetPosition) {
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
