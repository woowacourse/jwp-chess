package chess.dto.response;

public class RoomEnterResponse {
    public Long roomId;

    public RoomEnterResponse(final Long id) {
        this.roomId = id;
    }

    public Long getRoomId() {
        return roomId;
    }
}
