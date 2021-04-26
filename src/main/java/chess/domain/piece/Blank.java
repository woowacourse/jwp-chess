package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.position.Target;
import chess.exception.BlankMoveException;
import chess.exception.EmptyPositionException;

import java.util.List;

public final class Blank extends Piece {

    private static final String INITIAL_NAME = ".";

    public Blank(final Position position) {
        super(Color.NOTHING, INITIAL_NAME, position);
    }

    @Override
    public double score(final List<Piece> pieces) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Piece move(final Target target) {
        throw new BlankMoveException();
    }

    @Override
    public boolean canMove(final Target target) {
        throw new EmptyPositionException();
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isBlank() {
        return true;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public boolean canMultiMove() {
        return false;
    }
}
