package wooteco.chess.domain.piece.strategy;

import java.util.Map;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Path;
import wooteco.chess.domain.piece.Piece;

@FunctionalInterface
public interface MoveStrategy {
    Path findMovablePositions(Path path, Map<Position, Piece> pieces);
}
