package chess.model.dto;

import chess.model.entity.GameEntity;

public class GameInfoResponse {
    private Long gameId;
    private String name;

    private GameInfoResponse(Long gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public static GameInfoResponse from(GameEntity gameEntity) {
        return new GameInfoResponse(gameEntity.getGameId(), gameEntity.getName());
    }

    public Long getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }
}
