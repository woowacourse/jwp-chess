package wooteco.chess.domain.strategy.initialize;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

import java.util.Map;

public interface InitializeStrategy {
    Map<Position, Piece> initialize();
}
