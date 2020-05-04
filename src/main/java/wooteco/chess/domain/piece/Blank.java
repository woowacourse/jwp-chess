package wooteco.chess.domain.piece;

public class Blank extends Piece {
    public Blank(final PieceType pieceType) {
        super(pieceType);
    }

    @Override
    public Piece getNextPiece() throws IllegalArgumentException {
        throw new IllegalArgumentException("빈칸은 움직일 수 없습니다.");
    }
}
