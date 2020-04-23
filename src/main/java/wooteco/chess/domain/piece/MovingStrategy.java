package wooteco.chess.domain.piece;

import java.util.Map;
import java.util.Set;

import wooteco.chess.domain.position.Position;

public interface MovingStrategy {
	Set<Position> findMovablePositions(Position currentPosition, Map<Position, Piece> pieces);
}
