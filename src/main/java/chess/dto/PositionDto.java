package chess.dto;

public class PositionDto {
    private final String from;
    private final String to;

    public PositionDto(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }
}
