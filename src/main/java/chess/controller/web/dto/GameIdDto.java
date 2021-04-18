package chess.controller.web.dto;

public class GameIdDto {
    private Long gameId;

    public GameIdDto(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }
}
