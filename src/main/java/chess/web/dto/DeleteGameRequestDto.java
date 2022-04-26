package chess.web.dto;

public class DeleteGameRequestDto {

    private final int gameId;
    private final String password;

    public DeleteGameRequestDto(int gameId, String password) {
        this.gameId = gameId;
        this.password = password;
    }

    public int getGameId() {
        return gameId;
    }

    public String getPassword() {
        return password;
    }
}
