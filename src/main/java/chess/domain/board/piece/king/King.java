package chess.domain.board.piece.king;

import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Score;
import chess.domain.board.position.Position;
import chess.domain.direction.Direction;

import java.util.List;

public abstract class King extends Piece {

    private static final int MAX_DISTANCE = 1;

    private King(final Owner owner, final String symbol, final List<Direction> directions) {
        super(owner, symbol, directions);
    }

    public King(final Owner owner, final String symbol) {
        this(owner, symbol, Direction.allDirections());
    }

    public static King getInstanceOf(final Owner owner) {
        if (owner.isSame(Owner.BLACK)) {
            return BlackKing.getInstance();
        }

        if (owner.isSame(Owner.WHITE)) {
            return WhiteKing.getInstance();
        }

        throw new IllegalArgumentException("Invalid King");
    }

    @Override
    public Score score() {
        return Score.ZERO_SCORE;
    }

    @Override
    public int maxDistance() {
        return MAX_DISTANCE;
    }

    @Override
    public boolean isReachable(final Position source, final Position target, final Piece targetPiece) {
        return true;
    }

    @Override
    public boolean isOwnersKing(Owner owner) {
        return this.isSameOwner(owner);
    }
}