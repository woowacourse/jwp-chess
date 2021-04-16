package chess.dto;

import chess.domain.position.Position;
import java.beans.ConstructorProperties;

public class PathDto {

    private final String from;

    @ConstructorProperties({"from"})
    public PathDto(final String from) {
        this.from = from;
    }

    public Position getFrom() {
        return Position.of(from);
    }
}
