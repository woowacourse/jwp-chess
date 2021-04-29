package chess.controller.room;

import com.fasterxml.jackson.annotation.JsonProperty;

class RoomRequestDto {
    @JsonProperty("roomName")
    private String roomName;
    @JsonProperty("password")
    private String whiteUserPassword;

    public RoomRequestDto() {
    }

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
