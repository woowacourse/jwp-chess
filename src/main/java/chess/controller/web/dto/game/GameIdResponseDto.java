package chess.controller.web.dto.game;

public class GameIdResponseDto {
    private Long gameId;

    public GameIdResponseDto(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }
}
