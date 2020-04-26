package wooteco.chess.dto;

import javax.validation.constraints.NotEmpty;

public class RoomName {

    @NotEmpty
    private String roomName;

    public RoomName(final String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(final String roomName) {
        this.roomName = roomName;
    }
}
