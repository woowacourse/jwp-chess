package dto;

import chess.domain.game.LogIn;

public class LogInDto {

    private final String id;
    private final String password;

    public LogInDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public LogIn toEntity() {
        return new LogIn(id, password);
    }
}
