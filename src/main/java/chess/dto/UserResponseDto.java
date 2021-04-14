package chess.dto;

import chess.entity.UserEntity;

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

    public static UserResponseDto from(UserEntity userEntity) {
        return new UserResponseDto(userEntity.getId(), userEntity.getName(), userEntity.getCreatedTime());
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
