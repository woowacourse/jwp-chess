package wooteco.chess.domain.piece;

import wooteco.chess.exception.*;

public class Blank extends Piece {
    public Blank(final PieceType pieceType) {
        super(pieceType);
    }

    @Override
    public Piece getNextPiece() {
        throw new PieceImpossibleMoveException("빈칸은 움직일 수 없습니다.");
    }
}
