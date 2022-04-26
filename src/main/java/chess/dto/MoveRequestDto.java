package chess.dto;

public class MoveRequestDto {

    private String from;
    private String to;

    public MoveRequestDto() {

    }

    public MoveRequestDto(String from, String to) {
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
