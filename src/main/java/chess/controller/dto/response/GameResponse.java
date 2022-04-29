package chess.controller.dto.response;

public class GameResponse {

    private final long gameId;
    private final PlayersResponse playersResponse;
    private final boolean finished;
    private final ColorResponse currentTurnColor;

    public GameResponse(final long gameId, final PlayersResponse playersResponse,
                        final boolean finished, final ColorResponse currentTurnColor) {
        this.gameId = gameId;
        this.playersResponse = playersResponse;
        this.finished = finished;
        this.currentTurnColor = currentTurnColor;
    }

    public long getGameId() {
        return gameId;
    }

    public PlayersResponse getPlayersResponse() {
        return playersResponse;
    }

    public boolean isFinished() {
        return finished;
    }

    public ColorResponse getCurrentTurnColor() {
        return currentTurnColor;
    }
}
