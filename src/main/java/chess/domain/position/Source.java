package chess.domain.position;

import chess.domain.piece.Piece;

public class Source {
    private final Piece piece;

    public Source(final Piece piece) {
        this.piece = piece;
    }


    public Piece getPiece() {
        return piece;
    }

//    public void move(final Target target) {
//        piece.move(target);
//    }

    public boolean canMove(final Target target) {
        return piece.canMove(target);
    }
}
