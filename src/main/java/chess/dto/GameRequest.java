package chess.dto;

import chess.entity.GameEntity;

public class GameRequest {
    private final String title;
    private final String password;

    public GameRequest(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public GameEntity toGameEntity() {
        return new GameEntity(title, password);
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
