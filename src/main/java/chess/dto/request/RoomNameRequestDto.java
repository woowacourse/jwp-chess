package chess.dto.request;

import java.beans.ConstructorProperties;

public class RoomNameRequestDto {
    private final String roomName;

    @ConstructorProperties({"roomName"})
    public RoomNameRequestDto(final String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }
}
