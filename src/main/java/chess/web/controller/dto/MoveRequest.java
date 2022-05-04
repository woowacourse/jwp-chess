package chess.web.controller.dto;

public class MoveRequest {
    private String from;
    private String to;

    public MoveRequest(String from, String to) {
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
