package chess.dto;

import chess.entity.User;

import java.time.LocalDateTime;

public class UserResponseDto {

    private final long id;
    private final String name;
    private final LocalDateTime createdTime;

    private UserResponseDto(long id, String name, LocalDateTime createdTime) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getCreatedTime());
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
