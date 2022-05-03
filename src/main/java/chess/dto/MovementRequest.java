package chess.dto;

public class MovementRequest {
    private String source;
    private String target;

    public MovementRequest(String source, String target) {
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
