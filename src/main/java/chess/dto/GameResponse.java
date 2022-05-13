package chess.dto;

import chess.entity.GameEntity;

public class GameResponse {
    private final Long id;
    private final String title;

    public GameResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static GameResponse of(GameEntity gameEntity) {
        return new GameResponse(gameEntity.getId(), gameEntity.getTitle());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
