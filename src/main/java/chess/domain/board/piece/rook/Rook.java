package chess.domain.board.piece.rook;

import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Score;
import chess.domain.board.position.Position;
import chess.domain.direction.Direction;

import java.util.List;

public abstract class Rook extends Piece {

    private static final int MAX_DISTANCE = 7;

    private Rook(final Owner owner, final String symbol, final List<Direction> directions) {
        super(owner, symbol, directions);
    }

    protected Rook(final Owner owner, final String symbol) {
        this(owner, symbol, Direction.straightDirections());
    }

    public static Rook getInstanceOf(final Owner owner) {
        if (owner.equals(Owner.BLACK)) {
            return BlackRook.getInstance();
        }

        if (owner.equals(Owner.WHITE)) {
            return WhiteRook.getInstance();
        }

        throw new IllegalArgumentException("Invalid Rook");
    }

    @Override
    public Score score() {
        return Score.ROOK_SCORE;
    }

    @Override
    public int maxDistance() {
        return MAX_DISTANCE;
    }

    @Override
    public boolean isReachable(final Position source, final Position target, final Piece targetPiece) {
        return true;
    }
}
