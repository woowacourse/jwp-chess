package chess.dto;

import java.beans.ConstructorProperties;

public class PathDto {

    private final String from;

    @ConstructorProperties({"from"})
    public PathDto(final String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }
}
