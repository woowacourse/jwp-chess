package chess.dto;

import java.beans.ConstructorProperties;
import lombok.Getter;

@Getter
public class PathDto {

    private final String from;

    @ConstructorProperties({"from"})
    public PathDto(final String from) {
        this.from = from;
    }
}
