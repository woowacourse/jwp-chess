package chess.dto;

public class MovePositionRequest {

    private String from;
    private String to;

    public MovePositionRequest() {
    }

    public MovePositionRequest(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
