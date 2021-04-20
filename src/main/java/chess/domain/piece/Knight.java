package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;
import java.util.List;

public final class Knight extends GeneralPiece {

    private static final String INITIAL_NAME = "N";
    private static final double SCORE = 2.5;

    public Knight(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

    @Override
    public double score(final List<Piece> pieces) {
        return SCORE;
    }

    @Override
    protected MoveStrategies assignMoveStrategies() {
        return MoveStrategies.knightMoveStrategies();
    }

    @Override
    public Piece move(final Target target) {
        return new Knight(this.color(), target.piece().position());
    }

    @Override
    public boolean canMultiMove() {
        return false;
    }
}
