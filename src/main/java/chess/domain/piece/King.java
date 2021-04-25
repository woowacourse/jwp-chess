package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;

import java.util.List;

public final class King extends GeneralPiece {

    private static final String INITIAL_NAME = "K";
    private static final double SCORE = 0;

    public King(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

    @Override
    public double score(final List<Piece> pieces) {
        return SCORE;
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
    public boolean canMultiMove() {
        return false;
    }

    @Override
    public Piece move(final Target target) {
        return new King(this.color(), target.piece().position());
    }
}
