package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Path;
import wooteco.chess.domain.piece.Piece;

import java.util.Collections;
import java.util.Map;

public class BlankMoveStrategy implements MoveStrategy {
	@Override
	public Path findMovablePositions(final Path path, final Map<Position, Piece> pieces) {
		path.findPathOneTimeByDirections(Collections.EMPTY_LIST, pieces);
		return path;
	}
}
