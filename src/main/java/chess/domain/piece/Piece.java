package chess.domain.piece;

import java.util.Objects;

public abstract class Piece {

    private final Color color;
    private final String name;

    public Piece(final Color color, final String initialName) {
        this.color = color;
        if (color.isBlack()) {
            name = initialName.toUpperCase();
            return;
        }
        name = initialName.toLowerCase();
    }

    public String name() {
        return name;
    }

    public Color team() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Piece piece = (Piece) o;
        return Objects.equals(name, piece.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
