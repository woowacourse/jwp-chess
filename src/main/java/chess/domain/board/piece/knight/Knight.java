package chess.domain.board.piece.knight;

import chess.domain.board.position.Position;
import chess.domain.direction.Direction;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Score;

import java.util.List;

public abstract class Knight extends Piece {

    private static final int MAX_DISTANCE = 1;

    private Knight(final Owner owner, final String symbol, final List<Direction> directions) {
        super(owner, symbol, directions);
    }

    public Knight(final Owner owner, String symbol) {
        this(owner, symbol, Direction.knightDirections());
    }

    public static Knight getInstanceOf(final Owner owner) {
        if (owner.equals(Owner.BLACK)) {
            return BlackKnight.getInstance();
        }
        if (owner.equals(Owner.WHITE)) {
            return WhiteKnight.getInstance();
        }
        throw new IllegalArgumentException("Invalid Knight");
    }

    @Override
    public Score score() {
        return Score.KNIGHT_SCORE;
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
