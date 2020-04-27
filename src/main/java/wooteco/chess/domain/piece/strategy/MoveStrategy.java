package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Path;
import wooteco.chess.domain.piece.Piece;

import java.util.Map;

@FunctionalInterface
public interface MoveStrategy {
    Path findMovablePositions(Path path, Map<Position, Piece> pieces);
}
