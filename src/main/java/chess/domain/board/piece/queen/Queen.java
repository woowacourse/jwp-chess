package chess.domain.board.piece.queen;

import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Score;
import chess.domain.board.position.Position;
import chess.domain.direction.Direction;

import java.util.List;

public abstract class Queen extends Piece {

    private static final int MAX_DISTANCE = 7;

    private Queen(final Owner owner, final String symbol, final List<Direction> directions) {
        super(owner, symbol, directions);
    }

    protected Queen(final Owner owner, final String symbol) {
        this(owner, symbol, Direction.allDirections());
    }

    public static Queen getInstanceOf(final Owner owner) {
        if (owner.equals(Owner.BLACK)) {
            return BlackQueen.getInstance();
        }

        if (owner.equals(Owner.WHITE)) {
            return WhiteQueen.getInstance();
        }

        throw new IllegalArgumentException("Invalid Queen");
    }

    @Override
    public Score score() {
        return Score.QUEEN_SCORE;
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
