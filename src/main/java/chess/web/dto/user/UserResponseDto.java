package chess.web.dto.user;

import chess.domain.user.User;
import java.time.LocalDateTime;

public class UserResponseDto {

    private long id;
    private String name;
    private LocalDateTime createdTime;

    public UserResponseDto() {
    }

    private UserResponseDto(final long id, String name, final LocalDateTime createdTime) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
    }

    public static UserResponseDto from(final User user) {
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
