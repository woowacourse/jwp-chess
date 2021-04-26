package chess.controller.web.dto;

import java.beans.ConstructorProperties;

public class RoomRequestDto {
    private final String roomName;
    private final String whiteUserPassword;

    @ConstructorProperties({"roomName", "password"})
    public RoomRequestDto(String roomName, String whiteUserPassword) {
        this.roomName = roomName;
        this.whiteUserPassword = whiteUserPassword;
    }

    public String getRoomName() {
        return roomName;
    }


    public String getWhiteUserPassword() {
        return whiteUserPassword;
    }
}
