package chess.dto.request;

import chess.domain.position.Position;

public class MoveRequestDto {

    private String from;
    private String to;

    public MoveRequestDto() {
    }

    public MoveRequestDto(final String from, final String to) {
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return Position.from(from);
    }

    public Position getTo() {
        return Position.from(to);
    }
}
