package chess.dto.user;

import chess.dao.dto.UserDto;
import java.time.LocalDateTime;

public class UserRequestDto {

    private final String name;
    private final String password;

    public UserRequestDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public UserDto toUserDto() {
        return new UserDto(0L, name, password, LocalDateTime.now());
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
