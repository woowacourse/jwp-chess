package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;

public final class King extends GeneralPiece {

    private static final String INITIAL_NAME = "K";

    public King(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

    @Override
    protected MoveStrategies assignMoveStrategies() {
        return MoveStrategies.everyMoveStrategies();
    }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public Piece move(final Target target) {
        return new King(this.color(), target.getPiece().position());
    }
}
