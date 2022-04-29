package chess.dto;

public class MovePositionReq {

    private String from;
    private String to;

    public MovePositionReq() {
    }

    public MovePositionReq(String from, String to) {
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
