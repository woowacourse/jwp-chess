package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.position.Target;

import java.util.Objects;

public abstract class Piece {

    private final Color color;
    private final String name;
    private final Position position;

    public Piece(final Color color, final String initialName, final Position position) {
        this.color = color;
        this.position = position;
        if (color.isBlack()) {
            this.name = initialName.toUpperCase();
            return;
        }
        this.name = initialName.toLowerCase();
    }

    public String name() {
        return name;
    }

    public Color team() {
        return color;
    }

    public Position position(){
        return position;
    }

    public String positionInfo() {
        return position.position();
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

/*
    public abstract void move(final Target target);
*/

    public abstract boolean canMove(final Target target);

    public abstract boolean isPawn();

    public abstract boolean isBlank();

    public abstract boolean isKing();
}
