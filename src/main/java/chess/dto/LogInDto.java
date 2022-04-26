package chess.dto;

public class LogInDto {
    private String gameId;
    private String gamePassword;

    public LogInDto() {
    }

    public LogInDto(String gameId, String gamePassword) {
        this.gameId = gameId;
        this.gamePassword = gamePassword;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGamePassword() {
        return gamePassword;
    }

}
