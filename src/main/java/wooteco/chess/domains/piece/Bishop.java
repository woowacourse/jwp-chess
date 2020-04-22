package wooteco.chess.domains.piece;

import wooteco.chess.domains.position.Position;

public class Bishop extends Piece {

    public Bishop(PieceColor pieceColor) {
        super(pieceColor, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Position current, Position target) {
        return Math.abs(current.xGapBetween(target)) == Math.abs(current.yGapBetween(target));
    }
}
