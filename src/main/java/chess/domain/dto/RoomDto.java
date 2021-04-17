package chess.domain.dto;

public class RoomDto {

    private final String roomName;

    public RoomDto(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }
}
