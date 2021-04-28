package chess.controller.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomRequestDto {
    @JsonProperty
    private final String roomName;
    @JsonProperty("password")
    private final String whiteUserPassword;

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
