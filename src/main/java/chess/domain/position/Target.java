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
        result.add(piece.position().file().value() - source.piece().position().file().value());
        result.add(piece.position().rank().value() - source.piece().position().rank().value());
        return result;
    }

    public Piece piece() {
        return piece;
    }

    public Color color() {
        return piece.color();
    }

    public boolean isBlank() {
        return piece.isBlank();
    }

    public boolean isBlack() {
        return piece.color().isBlack();
    }

    public boolean isOpponent(final Source source) {
        return this.piece.color().isOppositeColor(source.piece().color());
    }

    public Position position() {
        return piece.position();
    }
}
