package chess.controller.dto;

import javax.validation.constraints.Size;

public class RoomDto {

    @Size(min=3, max = 12, message = "적절하지 않은 방 이름 길이")
    private String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
