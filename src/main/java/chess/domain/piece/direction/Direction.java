package chess.domain.piece.direction;

import chess.domain.position.Position;
import java.util.List;

public abstract class Direction implements MoveStrategy {
    private final int fileDegree;
    private final int rankDegree;

    protected Direction(final int fileDegree, final int rankDegree) {
        this.fileDegree = fileDegree;
        this.rankDegree = rankDegree;
    }

    @Override
    public Position move(final Position position) {
        return null;
    }

    @Override
    public boolean isSameDirection(final int fileDegree, final int rankDegree) {
        return this.fileDegree == fileDegree && this.rankDegree == rankDegree;
    }
}
