package chess.dto.user;

import chess.domain.user.User;
import java.time.LocalDateTime;

public class UserRequestDto {

    private final String name;
    private final String password;

    public UserRequestDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public User toEntity() {
        return new User(0L, name, password, LocalDateTime.now());
    }

}
