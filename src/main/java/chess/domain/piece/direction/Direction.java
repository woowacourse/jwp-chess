package chess.domain.piece.direction;

import chess.domain.position.Position;

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

    @Override
    public boolean isNothing() {
        return false;
    }

    @Override
    public boolean isNorth() {
        return false;
    }

    @Override
    public boolean isNorthWest() {
        return false;
    }

    @Override
    public boolean isNorthEast() {
        return false;
    }

    @Override
    public boolean isInitialPawnNorth() {
        return false;
    }

    @Override
    public int fileDegree() {
        return fileDegree;
    }

    @Override
    public int rankDegree() {
        return rankDegree;
    }
}
