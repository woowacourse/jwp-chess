package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;

public final class Queen extends GeneralPiece {

    private static final String INITIAL_NAME = "Q";

    public Queen(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

    @Override
    protected MoveStrategies assignMoveStrategies() {
        return MoveStrategies.everyMoveStrategies();
    }

    @Override
    public Piece move(final Target target) {
        return new Queen(this.color(), target.piece().position());
    }

    @Override
    public boolean canMultiMove() {
        return true;
    }
}
