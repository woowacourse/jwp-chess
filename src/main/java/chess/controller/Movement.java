package chess.controller;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.Objects;
import java.util.regex.Pattern;

public class Movement {

    private final Pattern pattern = Pattern.compile("[A-H][1-8]");

    private final Position from;
    private final Position to;

    public Movement(String from, String to) {
        checkPosition(from);
        checkPosition(to);
        this.from = new Position(file(from), rank(from));
        this.to = new Position(file(to), rank(to));
    }

    private void checkPosition(String position) {
        if (!pattern.matcher(position).matches()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    private File file(String position) {
        return File.valueOf(position.substring(0, 1));
    }

    private Rank rank(String position) {
        return Rank.find(position.substring(1, 2));
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movement)) {
            return false;
        }
        Movement movement = (Movement) o;
        return Objects.equals(from, movement.from) && Objects.equals(to, movement.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
