package chess.dto.response;

public class RoomListResponse {
    private final Long roomId;
    private final String roomName;

    public RoomListResponse(final Long roomId, final String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

}
