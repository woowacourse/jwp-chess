package chess.domain.piece;

import chess.domain.position.Movement;
import chess.domain.position.UnitDirection;

import java.util.List;

public abstract class MovingMultipleUnitPiece extends Piece {
    List<UnitDirection> movableDirections;

    MovingMultipleUnitPiece(Color color, double score, int moveCount, List<UnitDirection> directions) {
        super(color, score, moveCount);
        this.movableDirections = directions;
    }

    @Override
    public boolean canMove(Movement movement, Piece target) {
        return movement.hasMultiple(movableDirections);
    }

    @Override
    public boolean isNone() {
        return false;
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    abstract public boolean isRook();
}
