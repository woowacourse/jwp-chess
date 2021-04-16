package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;
import java.util.List;

public final class Bishop extends GeneralPiece {

    private static final String INITIAL_NAME = "B";
    private static final double SCORE = 3;

    public Bishop(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

    @Override
    public double score(final List<Piece> pieces) {
        return SCORE;
    }

    @Override
    protected MoveStrategies assignMoveStrategies() {
        return MoveStrategies.diagonalMoveStrategies();
    }

    @Override
    public Piece move(final Target target) {
        return new Bishop(this.color(), target.piece().position());
    }

    @Override
    public boolean canMultiMove() {
        return true;
    }
}
