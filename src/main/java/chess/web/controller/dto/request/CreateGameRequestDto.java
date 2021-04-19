package chess.web.controller.dto.request;

public class CreateGameRequestDto {

    private final String gameTitle;
    private final String rawWhitePlayerPassword;

    public CreateGameRequestDto(String gameTitle, String rawWhitePlayerPassword) {
        this.gameTitle = gameTitle;
        this.rawWhitePlayerPassword = rawWhitePlayerPassword;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public String getRawWhitePlayerPassword() {
        return rawWhitePlayerPassword;
    }
}
