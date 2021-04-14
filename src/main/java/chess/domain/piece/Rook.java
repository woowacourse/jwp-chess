package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;

public final class Rook extends GeneralPiece {

    private static final String INITIAL_NAME = "R";

    public Rook(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

    @Override
    protected MoveStrategies assignMoveStrategies() {
        return MoveStrategies.orthogonalMoveStrategies();
    }

    @Override
    public Piece move(final Target target) {
        return new Rook(this.color(), target.getPiece().position());
    }
}
