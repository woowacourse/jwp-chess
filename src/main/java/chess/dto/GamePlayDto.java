package chess.dto;

public class GamePlayDto {
    private String gameId;
    private String gameMessage;

    public GamePlayDto() {
    }

    public GamePlayDto(String gameId, String gameMessage) {
        this.gameId = gameId;
        this.gameMessage = gameMessage;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameMessage() {
        return gameMessage;
    }
}
