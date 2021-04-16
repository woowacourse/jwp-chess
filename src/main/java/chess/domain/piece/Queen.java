package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;
import java.util.List;

public final class Queen extends GeneralPiece {

    private static final String INITIAL_NAME = "Q";
    private static final double SCORE = 9;

    public Queen(final Color color, final Position position) {
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
    public Piece move(final Target target) {
        return new Queen(this.color(), target.piece().position());
    }

    @Override
    public boolean canMultiMove() {
        return true;
    }
}
