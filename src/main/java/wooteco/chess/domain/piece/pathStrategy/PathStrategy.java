package wooteco.chess.domain.piece.pathStrategy;

import java.util.List;

import wooteco.chess.domain.board.Position;

public abstract class PathStrategy {
	public List<Position> createPaths(Position sourcePosition, Position targetPosition) {
		validateDistance(sourcePosition, targetPosition);
		return findPaths(sourcePosition, targetPosition);
	}

	abstract protected void validateDistance(Position sourcePosition, Position targetPosition);

	abstract protected List<Position> findPaths(Position sourcePosition, Position targetPosition);
}
