package chess.dto;

public class PositionDto {
    private String from;
    private String to;

    public PositionDto() {
    }

    public PositionDto(String from, String to) {
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
