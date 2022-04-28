package chess.dto;

import chess.domain.game.Game;

public class GameDto {

    private final long gameId;
    private final PlayersDto playersDto;
    private final boolean finished;
    private final String currentTurnColor;

    private GameDto(final long gameId, final PlayersDto playersDto, final boolean finished, final String currentTurnColor) {
        this.gameId = gameId;
        this.playersDto = playersDto;
        this.finished = finished;
        this.currentTurnColor = currentTurnColor;
    }

    public static GameDto toDto(final Game game) {
        return new GameDto(
                game.getId(), PlayersDto.toDto(game.getPlayers()),
                game.isFinished(), game.getColorOfCurrentTurn().getName());
    }

    public long getGameId() {
        return gameId;
    }

    public PlayersDto getPlayersDto() {
        return playersDto;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getCurrentTurnColor() {
        return currentTurnColor;
    }
}
