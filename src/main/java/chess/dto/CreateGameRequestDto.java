package chess.dto;

public class CreateGameRequestDto {

    private final String title;
    private final String password;
    private final Long whiteId;
    private final Long blackId;

    public CreateGameRequestDto(final String title, final String password, final Long whiteId, final Long blackId) {
        this.title = title;
        this.password = password;
        this.whiteId = whiteId;
        this.blackId = blackId;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public Long getWhiteId() {
        return whiteId;
    }

    public Long getBlackId() {
        return blackId;
    }
}
