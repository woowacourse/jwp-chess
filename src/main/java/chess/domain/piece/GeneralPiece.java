package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;

public abstract class GeneralPiece extends Piece {
    private final MoveStrategies moveStrategies;

    public GeneralPiece(final Color color, final String initialName, final Position position) {
        super(color, initialName, position);
        this.moveStrategies = assignMoveStrategies();
    }

    protected abstract MoveStrategies assignMoveStrategies();
}
