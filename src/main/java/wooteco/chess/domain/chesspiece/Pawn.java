package wooteco.chess.domain.chesspiece;

import static wooteco.chess.domain.Direction.*;

import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.Direction;
import wooteco.chess.domain.MoveManager;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

public class Pawn extends Piece {
	private static final String NAME = "p";

	public Pawn(Position position, Team team) {
		super(position, team);
	}

	@Override
	public String getName() {
		return team.parseName(NAME);
	}

	@Override
	public boolean isNotNeedCheckPath() {
		return isNotMoved() == false;
	}

	private boolean isNotMoved() {
		if (team == Team.WHITE) {
			return position.isRowEquals(2);
		}
		return position.isRowEquals(7);
	}

	@Override
	public Positions makePathAndValidate(Piece targetPiece) {
		validateCanGo(targetPiece);
		return moveManager.makePath(
			targetPiece.position, getCanGoDirections());
	}

	@Override
	public void validateCanGo(Piece targetPiece) {
		Direction direction = moveManager.getMatchDirection(targetPiece.position);
		moveManager.validateMove(direction, getCanGoDirections());
		if (cannotMove(direction, targetPiece)) {
			throw new IllegalArgumentException(MoveManager.CANNOT_MOVE_POSITION);
		}
	}

	private List<Direction> getCanGoDirections() {
		List<Direction> directions = new ArrayList<>();
		directions.addAll(Direction.getPawnDirections(team));
		if (!isNotMoved()) {
			return directions;
		}
		if (team == Team.WHITE) {
			directions.add(DOUBLE_UP);
			return directions;
		}
		directions.add(DOUBLE_DOWN);
		return directions;
	}

	private boolean cannotMove(Direction direction, Piece targetPiece) {
		return canMoveForward(direction, targetPiece) == false
			&& canToCatch(direction, targetPiece) == false;
	}

	private boolean canMoveForward(Direction direction, Piece piece) {
		return isCanMoveDirection(direction) && piece.isBlankPiece();
	}

	private boolean isCanMoveDirection(Direction direction) {
		return direction == UP || direction == DOWN
			|| direction == DOUBLE_UP || direction == DOUBLE_DOWN;
	}

	private boolean canToCatch(Direction direction, Piece targetPiece) {
		return isCanCatchDirection(direction) && targetPiece.isNotBlankPiece();
	}

	private boolean isCanCatchDirection(Direction direction) {
		return direction == RIGHT_UP || direction == LEFT_UP
			|| direction == RIGHT_DOWN || direction == LEFT_DOWN;
	}
}