package chess.dao.entity;

import chess.domain.GameState;

public class GameEntity {

    private final Long id;
    private final String name;
    private final String password;
    private final String salt;
    private final GameState state;

    private GameEntity(Long id, String name, String password, String salt, GameState state) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.state = state;
    }

    public static GameEntity toSave(String name, String password, String salt, GameState state) {
        return new GameEntity(null, name, password, salt, state);
    }

    public static GameEntity toFind(Long id, String name, String password, String salt, GameState state) {
        return new GameEntity(id, name, password, salt, state);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public GameState getState() {
        return state;
    }
}
