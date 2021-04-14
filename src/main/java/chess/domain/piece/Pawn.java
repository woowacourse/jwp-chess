package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;

public final class Pawn extends Piece {

    private static final String INITIAL_NAME = "P";
    private final MoveStrategies moveStrategies;

    public Pawn(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
        this.moveStrategies = MoveStrategies.pawnMoveStrategies();
    }

    @Override
    public boolean canMove(Target target) {
        return false;
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }
}
