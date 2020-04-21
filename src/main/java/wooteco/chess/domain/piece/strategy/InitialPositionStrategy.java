package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Side;

@FunctionalInterface
public interface InitialPositionStrategy {
    boolean shouldBePlacedOn(final Position position, final Side side);
}
