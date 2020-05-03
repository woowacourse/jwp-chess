package wooteco.chess.dto;

public class GameRequestDto {

    private Long id;

    public GameRequestDto() {
    }

    public GameRequestDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
