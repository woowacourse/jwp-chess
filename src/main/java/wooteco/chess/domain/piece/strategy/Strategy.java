package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.Row;
import wooteco.chess.domain.piece.Side;

public interface Strategy extends InitialPositionStrategy, MovingStrategy {

    default boolean isNotPawn(final Position position, final Side side) {
        return side == Side.WHITE && position.isOn(Row.ONE)
            || side == Side.BLACK && position.isOn(Row.EIGHT);
    }

    static Strategy empty() {
        return new Strategy() {
            @Override
            public boolean shouldBePlacedOn(final Position position, final Side side) {
                return false;
            }

            @Override
            public boolean isMovableWithin(final Path path) {
                return false;
            }
        };
    }
}
