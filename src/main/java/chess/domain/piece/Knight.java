package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;

public final class Knight extends GeneralPiece {

    private static final String INITIAL_NAME = "N";

    public Knight(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

    @Override
    protected MoveStrategies assignMoveStrategies() {
        return MoveStrategies.knightMoveStrategies();
    }

    @Override
    public Piece move(final Target target) {
        return new Knight(this.color(), target.getPiece().position());
    }
}
