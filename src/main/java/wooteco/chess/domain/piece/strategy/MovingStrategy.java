package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.board.Path;

@FunctionalInterface
public interface MovingStrategy {
    boolean isMovableWithin(Path path);
}
