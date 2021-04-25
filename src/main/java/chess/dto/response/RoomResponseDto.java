package chess.dto.response;

public class RoomResponseDto {
    private final long roomId;
    private final String roomName;

    public RoomResponseDto(final long roomId, final String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
