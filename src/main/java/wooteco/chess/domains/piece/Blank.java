package wooteco.chess.domains.piece;

import wooteco.chess.domains.position.Position;

public class Blank extends Piece {
    public Blank() {
        super(PieceColor.BLANK, PieceType.BLANK);
    }

    @Override
    public boolean isValidMove(Position currentPosition, Position targetPosition) {
        return false;
    }
}
