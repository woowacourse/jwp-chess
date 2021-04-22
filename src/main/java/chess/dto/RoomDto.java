package chess.dto;

public class RoomDto {
    private final String roomId;
    private final String roomName;

    public RoomDto(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
