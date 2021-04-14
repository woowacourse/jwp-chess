package chess.domain.position;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import java.util.ArrayList;
import java.util.List;

public class Target {
    private final Piece piece;

    public Target(final Piece piece) {
        this.piece = piece;
    }

    public List<Integer> subtract(final Source source) {
        List<Integer> result = new ArrayList<>();
        result.add(piece.position().file().value() - source.getPiece().position().file().value());
        result.add(piece.position().rank().value() - source.getPiece().position().rank().value());
        return result;
    }

    public Piece getPiece() {
        return piece;
    }

    public Color color() {
        return piece.color();
    }

    public boolean isBlank() {
        return piece.isBlank();
    }
}
