package chess.dto;

import chess.domain.User;

public class UserDto {
    private final String name;
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
