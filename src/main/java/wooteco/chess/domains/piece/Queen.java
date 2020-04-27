package wooteco.chess.domains.piece;

import wooteco.chess.domains.position.Position;

public class Queen extends Piece {
    public Queen(PieceColor pieceColor) {
        super(pieceColor, PieceType.QUEEN);
    }

    @Override
    public boolean isValidMove(Position current, Position target) {
        return current.isSameX(target)
                || current.isSameY(target)
                || (Math.abs(current.xGapBetween(target)) == Math.abs(current.yGapBetween(target)));
    }
}
