package chess.dto;

import java.beans.ConstructorProperties;
import lombok.Getter;

@Getter
public class MoveRequest {

    private final String from;
    private final String to;

    @ConstructorProperties({"from", "to"})
    public MoveRequest(final String from, final String to) {
        this.from = from;
        this.to = to;
    }
}
