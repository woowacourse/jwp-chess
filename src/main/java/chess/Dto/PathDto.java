package chess.Dto;

import chess.domain.position.Position;

public class PathDto {

    private String from;

    //deserialization purpose
    public PathDto() {}

    public PathDto(final String from) {
        this.from = from;
    }

    public Position getFrom() {
        return Position.of(from);
    }
}
