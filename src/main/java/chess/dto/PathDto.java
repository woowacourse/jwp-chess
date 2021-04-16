package chess.dto;

import chess.domain.position.Position;

public class PathDto {

    private String from;

    //deserialization purpose
    public PathDto(final String from) {
        this.from = from;
    }

    protected PathDto() {
    }

    public Position getFrom() {
        return Position.of(from);
    }
}
