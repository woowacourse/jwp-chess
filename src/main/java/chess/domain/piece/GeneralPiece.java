package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;

public abstract class GeneralPiece extends Piece {
    private final MoveStrategies moveStrategies;

    public GeneralPiece(final Color color, final String initialName, final Position position) {
        super(color, initialName, position);
        this.moveStrategies = assignMoveStrategies();
    }

    @Override
    public boolean canMove(final Target target) {
        return false;
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    protected abstract MoveStrategies assignMoveStrategies();
}
