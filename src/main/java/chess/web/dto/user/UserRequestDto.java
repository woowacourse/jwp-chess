package chess.web.dto.user;

import chess.domain.user.User;
import java.time.LocalDateTime;

public class UserRequestDto {

    private String name;
    private String password;

    public UserRequestDto() {
    }

    public UserRequestDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public User toEntity() {
        return new User(0L, name, password, LocalDateTime.now());
    }


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
