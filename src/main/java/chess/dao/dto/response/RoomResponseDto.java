package chess.dao.dto.response;

public class RoomResponseDto {
    private final long roomId;
    private final String roomName;
    private final String currentTurn;

    public RoomResponseDto(final long roomId, final String roomName, final String currentTurn) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.currentTurn = currentTurn;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}
