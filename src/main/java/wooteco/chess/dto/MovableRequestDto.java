package wooteco.chess.dto;

public class MovableRequestDto {
    private final String from;

    public MovableRequestDto(final String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }
}
