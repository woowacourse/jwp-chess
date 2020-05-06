package wooteco.chess.dto.request;

public class MoveRequest {
    private String source;
    private String target;

    protected MoveRequest() {
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
