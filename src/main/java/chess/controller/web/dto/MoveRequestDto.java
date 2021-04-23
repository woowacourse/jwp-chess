package chess.controller.web.dto;

import chess.domain.position.Position;

import java.util.Objects;

public class MoveRequestDto {
    private final String from;
    private final String to;

    public MoveRequestDto(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Position getFromPosition() {
        return Position.of(from);
    }

    public Position getToPosition() {
        return Position.of(to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveRequestDto that = (MoveRequestDto) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
