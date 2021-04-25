package dto;

import chess.domain.User;

import javax.validation.constraints.Size;

public class UserDto {
    @Size(min = 2, max = 4)
    private final String name;
    @Size(min = 2, max = 8)
    private final String pw;
    private final boolean inGame;

    private UserDto(@Size(min = 2, max = 4) final String name, @Size(min = 2, max = 8) final String pw, final boolean inGame) {
        this.name = name;
        this.pw = pw;
        this.inGame = inGame;
    }

    public static UserDto toResponse(User user) {
        return new UserDto(user.getName(), "", user.inGame());
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }

    public boolean isInGame() {
        return inGame;
    }
}
