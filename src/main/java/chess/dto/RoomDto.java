package chess.dto;

public class RoomDto {

    private final String roomName;
    private final int roomId;

    public RoomDto(final String roomName, final int roomId) {
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomId() {
        return roomId;
    }
}
