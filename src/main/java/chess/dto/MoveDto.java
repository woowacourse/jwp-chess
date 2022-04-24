package chess.dto;

public class MoveDto {

    private String from;
    private String to;

    public MoveDto(String from, String to) {
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
