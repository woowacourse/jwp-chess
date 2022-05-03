package chess.dto;

import javax.validation.constraints.NotEmpty;

public class MoveRequest {

    @NotEmpty
    private String source;
    @NotEmpty
    private String target;

    private MoveRequest() {
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
