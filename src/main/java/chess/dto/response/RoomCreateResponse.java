package chess.dto.response;

public class RoomCreateResponse {
    public Long roomId;

    public RoomCreateResponse(final Long id) {
        this.roomId = id;
    }

    public Long getRoomId() {
        return roomId;
    }
}
