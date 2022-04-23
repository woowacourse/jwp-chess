package chess.dto;

public class CreateGameRequestDto {

    private final Long whiteId;
    private final Long blackId;

    public CreateGameRequestDto(final Long whiteId, final Long blackId) {
        this.whiteId = whiteId;
        this.blackId = blackId;
    }

    public Long getWhiteId() {
        return whiteId;
    }

    public Long getBlackId() {
        return blackId;
    }
}
