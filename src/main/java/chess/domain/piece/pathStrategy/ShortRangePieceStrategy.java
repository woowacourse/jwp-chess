package chess.domain.piece.pathStrategy;

import static chess.util.NullValidator.*;

import java.util.ArrayList;
import java.util.List;

import chess.domain.board.Position;

public abstract class ShortRangePieceStrategy extends PathStrategy {
	@Override
	public List<Position> findPaths(Position sourcePosition, Position targetPosition) {
		validateNull(targetPosition);

		List<Position> path = new ArrayList<>();
		path.add(targetPosition);

		return path;
	}
}
