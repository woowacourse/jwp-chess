package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.board.Column;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Side;

public class RookStrategy implements Strategy {
    @Override
    public boolean shouldBePlacedOn(final Position position, final Side side) {
        return isNotPawn(position, side)
            && (position.isOn(Column.A) || position.isOn(Column.H));
    }

    @Override
    public boolean isMovableWithin(final Path path) {
        return path.isStraight()
            && (path.isEndEmpty() || path.isEnemyOnEnd())
            && !path.isBlocked();
    }
}
