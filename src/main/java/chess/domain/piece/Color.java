package chess.domain.piece;

import chess.domain.position.Position;

public enum Color {

    BLACK, WHITE;

    public boolean isForward(Position from, Position to) {
        if (this == WHITE) {
            return from.isUpward(to);
        }
        return from.isDownward(to);
    }

    public Color reverse() {
        if (this == WHITE) {
            return BLACK;
        }
        return WHITE;
    }

    public boolean equals(String color) {
        return this.name().equals(color.toUpperCase());
    }
}
