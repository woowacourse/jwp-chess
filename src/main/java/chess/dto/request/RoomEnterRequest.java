package chess.dto.request;

public class RoomEnterRequest {
    private final Long roomId;
    private final String userName;
    private final String roomPw;

    public RoomEnterRequest(final Long roomId, final String userName, final String roomPw) {
        this.roomId = roomId;
        this.userName = userName;
        this.roomPw = roomPw;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomPw() {
        return roomPw;
    }
}
