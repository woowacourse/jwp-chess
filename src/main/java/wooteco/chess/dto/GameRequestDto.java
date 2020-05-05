package wooteco.chess.dto;

public class GameRequestDto {
    private final String title;

    public GameRequestDto(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
