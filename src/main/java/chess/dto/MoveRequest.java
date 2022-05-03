package chess.dto;

public class MoveRequest {
    private int id;
    private String from;
    private String to;

    public MoveRequest(int id, String from, String to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
