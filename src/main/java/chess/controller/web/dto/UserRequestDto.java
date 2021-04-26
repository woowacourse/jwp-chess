package chess.controller.web.dto;

import java.beans.ConstructorProperties;

public class UserRequestDto {
    private final long roomId;
    private final String password;

    @ConstructorProperties({"roomId", "password"})
    public UserRequestDto(long roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}
