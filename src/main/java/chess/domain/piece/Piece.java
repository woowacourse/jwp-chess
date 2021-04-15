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

    public Color color() {
        return color;
    }

    public Position position() {
        return position;
    }

    public String positionInfo() {
        return position.position();
    }

    public boolean isSamePosition(final Position position){
        return this.position.equals(position);
    }

    public boolean isBlack() {
        return color().isBlack();
    }

    public abstract Piece move(final Target target);

    protected boolean isOpponent(final Target target) {
        return color.isOppositeColor(target.color());
    }

    public abstract boolean canMove(final Target target);

    public abstract boolean isPawn();

    public abstract boolean isBlank();

    public abstract boolean isKing();

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

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", name='" + name + '\'' +
                ", position=" + position +
                '}';
    }
}
