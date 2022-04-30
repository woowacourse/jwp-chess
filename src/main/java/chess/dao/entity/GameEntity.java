package chess.dao.entity;

import chess.domain.GameState;

public class GameEntity {

    private Long id;
    private String name;
    private String password;
    private String salt;
    private GameState state;

    public GameEntity(Long id, String name, String password, String salt, GameState state) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.state = state;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
