package chess.dto.response;

public class UserResponse {
    private final Long roomId;
    private final String userName;

    public UserResponse(final Long roomId, final String userName) {
        this.roomId = roomId;
        this.userName = userName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getUserName() {
        return userName;
    }
}
