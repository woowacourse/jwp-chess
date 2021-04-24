package chess.web.controller.dto.response;

public class CreateGameResponseDto {

    private final Long gameId;
    private final String encryptedWhitePlayerPassword;

    public CreateGameResponseDto(Long gameId, String encryptedWhitePlayerPassword) {
        this.gameId = gameId;
        this.encryptedWhitePlayerPassword = encryptedWhitePlayerPassword;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getEncryptedWhitePlayerPassword() {
        return encryptedWhitePlayerPassword;
    }
}
