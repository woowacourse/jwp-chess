package wooteco.chess.domain.piece.pathStrategy;

import static wooteco.chess.util.NullValidator.*;

import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.direction.Direction;

public abstract class LongRangePieceStrategy extends PathStrategy {
	public List<Position> findPaths(Position sourcePosition, Position targetPosition) {
		validateNull(sourcePosition, targetPosition);

		List<Position> path = new ArrayList<>();
		Direction direction = getDirection(sourcePosition, targetPosition);

		Position currentPosition = sourcePosition;
		while (!currentPosition.equals(targetPosition)) {
			Position changePosition = currentPosition.changeTo(direction);
			currentPosition = changePosition;
			path.add(changePosition);
		}

		return path;
	}

	private Direction getDirection(Position sourcePosition, Position targetPosition) {
		int xPointDirectionValue = sourcePosition.getXPointDirectionValueTo(targetPosition);
		int yPointDirectionValue = sourcePosition.getYPointDirectionValueTo(targetPosition);
		return Direction.of(xPointDirectionValue, yPointDirectionValue);
	}
}
