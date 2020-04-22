package wooteco.chess.domains.piece;

import wooteco.chess.domains.position.Position;

public class Rook extends Piece {
    public Rook(PieceColor pieceColor) {
        super(pieceColor, PieceType.ROOK);
    }

    @Override
    public boolean isValidMove(Position current, Position target) {
        return current.isSameX(target) || current.isSameY(target);
    }
}
