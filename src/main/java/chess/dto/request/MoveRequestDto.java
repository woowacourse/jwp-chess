package chess.dto.request;

public class MoveRequestDto {

    private String from;
    private String to;

    protected MoveRequestDto() {
    }

    public MoveRequestDto(final String from, final String to) {
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
