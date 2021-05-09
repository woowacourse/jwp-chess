package chess.domain.board.piece.pawn;

import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Score;
import chess.domain.board.position.Position;
import chess.domain.board.position.Vertical;
import chess.domain.direction.Direction;

import java.util.List;

public abstract class Pawn extends Piece {

    private static final int MAX_DISTANCE = 2;

    protected Pawn(final Owner owner, final String symbol, final List<Direction> directions) {
        super(owner, symbol, directions);
    }

    public static Pawn getInstanceOf(final Owner owner) {
        if (owner.isSame(Owner.BLACK)) {
            return BlackPawn.getInstance();
        }
        if (owner.isSame(Owner.WHITE)) {
            return WhitePawn.getInstance();
        }
        throw new IllegalArgumentException("Invalid Pawn");
    }

    private boolean isValidStraightMove(final Position source, final Position target) {
        if (this.isFirstLine(source.getVertical())) {
            return true;
        }
        return source.getDistance(target) == 1;
    }

    private boolean isValidDiagonalMove(final Position source, final Position target, final boolean isEnemy) {
        if (source.isDiagonal(target) && isEnemy) {
            return source.getDistance(target) == 1;
        }
        return false;
    }

    @Override
    public Score score() {
        return Score.PAWN_SCORE;
    }

    @Override
    public int maxDistance() {
        return MAX_DISTANCE;
    }

    @Override
    public boolean isReachable(final Position source, final Position target, final Piece targetPiece) {
        if (source.isStraight(target) && targetPiece.isEmptyPiece()) {
            return this.isValidStraightMove(source, target);
        }
        return this.isValidDiagonalMove(source, target, this.isEnemy(targetPiece));
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    protected abstract boolean isFirstLine(final Vertical vertical);
}