package chess.controller.dto.response;

public class GameResponse {

    private final Long gameId;
    private final PlayersResponse playersResponse;
    private final Boolean finished;
    private final Boolean promotable;
    private final ColorResponse currentTurnColor;

    public GameResponse(final Long gameId, final PlayersResponse playersResponse,
                        final Boolean finished, final Boolean promotable,
                        final ColorResponse currentTurnColor) {
        this.gameId = gameId;
        this.playersResponse = playersResponse;
        this.finished = finished;
        this.promotable = promotable;
        this.currentTurnColor = currentTurnColor;
    }

    public Long getGameId() {
        return gameId;
    }

    public PlayersResponse getPlayersResponse() {
        return playersResponse;
    }

    public Boolean isFinished() {
        return finished;
    }

    public Boolean isPromotable() {
        return promotable;
    }

    public ColorResponse getCurrentTurnColor() {
        return currentTurnColor;
    }
}
