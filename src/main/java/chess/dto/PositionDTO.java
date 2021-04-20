package chess.dto;

public class PositionDTO {
    private String from;
    private String to;

    public PositionDTO() {
    }

    public PositionDTO(String from, String to) {
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
