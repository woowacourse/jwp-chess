package chess.controller.dto.request;

public class MoveRequest {

    private String start;
    private String target;

    public MoveRequest() {
    }

    public MoveRequest(String start, String target) {
        this.start = start;
        this.target = target;
    }

    public String getStart() {
        return start;
    }

    public String getTarget() {
        return target;
    }
}
