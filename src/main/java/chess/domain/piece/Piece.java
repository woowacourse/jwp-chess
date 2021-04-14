package chess.domain.piece;

import java.util.Locale;
import java.util.Objects;

public abstract class Piece {

    private final Team team;
    private final String name;

    public Piece(final Team team, final String initialName) {
        this.team = team;
        if (team.isBlackTeam()) {
            name = initialName.toUpperCase();
            return;
        }
        name = initialName.toLowerCase();
    }

    public String name() {
        return name;
    }

    public Team team() {
        return team;
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
