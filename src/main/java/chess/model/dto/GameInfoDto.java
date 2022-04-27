package chess.model.dto;

import chess.entity.GameEntity;

public class GameInfoDto {
    private Long gameId;
    private String name;

    private GameInfoDto(Long gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public static GameInfoDto from(GameEntity gameEntity) {
        return new GameInfoDto(gameEntity.getGameId(), gameEntity.getName());
    }
}
