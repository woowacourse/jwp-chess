package chess.domain.board.piece.bishop;

import chess.domain.board.position.Position;
import chess.domain.direction.Direction;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Score;

import java.util.List;

public abstract class Bishop extends Piece {
    private static final int MAX_DISTANCE = 7;

    private Bishop(final Owner owner, final String symbol, final List<Direction> directions) {
        super(owner, symbol, directions);
    }

    public Bishop(final Owner owner, final String symbol) {
        this(owner, symbol, Direction.diagonalDirections());
    }

    public static Bishop getInstanceOf(final Owner owner) {
        if (owner.equals(Owner.BLACK)) {
            return BlackBishop.getInstance();
        }

        if (owner.equals(Owner.WHITE)) {
            return WhiteBishop.getInstance();
        }
        throw new IllegalArgumentException("Invalid Bishop");
    }

    @Override
    public Score score() {
        return Score.BISHOP_SCORE;
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
