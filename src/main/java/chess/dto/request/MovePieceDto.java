package chess.dto.request;

import chess.domain.position.Position;

public class MovePieceDto {
    private final String from;
    private final String to;

    public MovePieceDto(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Position getFromAsPosition() {
        return Position.from(from);
    }

    public Position getToAsPosition() {
        return Position.from(to);
    }

    @Override
    public String toString() {
        return "MovePieceDto{" +
            "from='" + from + '\'' +
            ", to='" + to + '\'' +
            '}';
    }
}
