package chess.controller.dto;

public class ChessPieceMoveRequest {

    private final String from;
    private final String to;

    public ChessPieceMoveRequest() {
        this.from = "";
        this.to = "";
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
