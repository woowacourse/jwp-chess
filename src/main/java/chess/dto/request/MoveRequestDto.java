package chess.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveRequestDto {

    private final String source;
    private final String target;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MoveRequestDto(@JsonProperty("source") final String source, @JsonProperty("target") final String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
