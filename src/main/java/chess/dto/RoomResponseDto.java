package chess.dto;

public class RoomResponseDto {

    private final String roomName;
    private final long roomId;

    public RoomResponseDto(String roomName, long roomId) {
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public long getRoomId() {
        return roomId;
    }

}
