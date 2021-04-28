package dto;

import chess.domain.User;

import javax.validation.constraints.Size;

public class UserDto {
    @Size(min = 2, max = 4)
    private final String name;
    @Size(min = 2, max = 8)
    private final String pw;
    private final Long roomId;

    public UserDto(final String name, final String pw, final Long roomId) {
        this.name = name;
        this.pw = pw;
        this.roomId = roomId;
    }

    public static UserDto toResponse(User user) {
        return new UserDto(user.getName(), "", user.getRoomId());
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }

    public Long getRoomId() {
        return roomId;
    }
}
