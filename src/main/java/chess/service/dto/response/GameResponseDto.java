package chess.service.dto.response;

public class GameResponseDto {

    private final Long gameId;
    private final PlayersResponseDto playersResponseDto;
    private final Boolean finished;
    private final Boolean promotable;
    private final ColorResponseDto currentTurnColor;

    public GameResponseDto(final Long gameId, final PlayersResponseDto playersResponseDto,
                           final Boolean finished, final Boolean promotable,
                           final ColorResponseDto currentTurnColor) {
        this.gameId = gameId;
        this.playersResponseDto = playersResponseDto;
        this.finished = finished;
        this.promotable = promotable;
        this.currentTurnColor = currentTurnColor;
    }

    public Long getGameId() {
        return gameId;
    }

    public PlayersResponseDto getPlayersResponseDto() {
        return playersResponseDto;
    }

    public Boolean getFinished() {
        return finished;
    }

    public Boolean getPromotable() {
        return promotable;
    }

    public ColorResponseDto getCurrentTurnColor() {
        return currentTurnColor;
    }
}
