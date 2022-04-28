package chess.service.dto.response;

public class DeleteGameResponse {
    private int gameId;
    private boolean success;

    public DeleteGameResponse() {
    }

    public DeleteGameResponse(int gameId, boolean success) {
        this.gameId = gameId;
        this.success = success;
    }

    public int getGameId() {
        return gameId;
    }

    public boolean isSuccess() {
        return success;
    }
}
