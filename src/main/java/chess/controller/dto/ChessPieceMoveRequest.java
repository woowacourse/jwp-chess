package chess.controller.dto;

public class ChessPieceMoveRequest {

    private final String from;
    private final String to;

    ChessPieceMoveRequest() {
        this.from = "";
        this.to = "";
    }

    public ChessPieceMoveRequest(String from, String to) {
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
