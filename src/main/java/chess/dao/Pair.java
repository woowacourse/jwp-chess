package chess.dao;

import chess.domain.board.Position;
import chess.domain.piece.Piece;

class Pair {

    private Position position;
    private Piece piece;

    public Pair(Position position, Piece piece) {
        this.position = position;
        this.piece = piece;
    }

    public Position getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }
}
