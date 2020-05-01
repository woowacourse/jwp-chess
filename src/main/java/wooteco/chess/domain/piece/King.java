package wooteco.chess.domain.piece;

import wooteco.chess.domain.coordinate.Vector;

public class King extends Piece {

    private static final int KING_SCORE = 0;
    private static final int KING_MOVE_RANGE = 1;

    public King(final Team team) {
        super(team, KING_SCORE);
    }

    @Override
    public boolean canMove(final Vector vector, final Piece targetPiece) {
        if (targetPiece.isSameTeam(this.team)) {
            return false;
        }
        return vector.isRangeUnderAbsolute(KING_MOVE_RANGE);
    }

    @Override
    public boolean isKing() {
        return true;
    }
}
