package chess.dto;

public class MoveRequest {

    private String source;
    private String target;

    public MoveRequest() {
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
