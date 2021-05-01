package chess.dao.dto;

import chess.domain.user.User;
import java.time.LocalDateTime;

public class UserDto {

    private final long id;
    private final String name;
    private final String password;
    private final LocalDateTime createdTime;

    private UserDto(final long id, final String name, final String password,
        final LocalDateTime createdTime) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createdTime = createdTime;
    }

    public static UserDto of(final long id, final String name, final String password,
        final LocalDateTime createdTime) {

        return new UserDto(id, name, password, createdTime);
    }

    public static UserDto from(final User user) {
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getPassword(),
            user.getCreatedTime()
        );
    }

    public User toEntity() {
        return new User(id, name, password, createdTime);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

}
