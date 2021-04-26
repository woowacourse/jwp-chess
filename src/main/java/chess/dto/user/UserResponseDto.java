package chess.dto.user;

import chess.dao.dto.UserDto;
import java.time.LocalDateTime;

public class UserResponseDto {

    private final long id;
    private final String name;
    private final LocalDateTime createdTime;

    private UserResponseDto(final long id, String name, final LocalDateTime createdTime) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
    }

    public static UserResponseDto from(final UserDto userDto) {
        return new UserResponseDto(userDto.getId(), userDto.getName(), userDto.getCreatedTime());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

}
