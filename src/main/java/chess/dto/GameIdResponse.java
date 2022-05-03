package chess.dto;

public class GameIdResponse {
    private String gameId;

    public GameIdResponse(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }
}
