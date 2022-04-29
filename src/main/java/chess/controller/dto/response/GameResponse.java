package chess.controller.dto.response;

public class GameResponse {

    private final long gameId;
    private final PlayersResponse playersResponse;
    private final boolean finished;
    private final boolean promotable;
    private final ColorResponse currentTurnColor;

    public GameResponse(final long gameId, final PlayersResponse playersResponse,
                        final boolean finished, final boolean promotable,
                        final ColorResponse currentTurnColor) {
        this.gameId = gameId;
        this.playersResponse = playersResponse;
        this.finished = finished;
        this.promotable = promotable;
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

    public boolean isPromotable() {
        return promotable;
    }

    public ColorResponse getCurrentTurnColor() {
        return currentTurnColor;
    }
}
