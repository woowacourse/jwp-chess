package chess.dto.response;

public class UserResponse {
    private final Long roomId;
    private final String userUser;


    public UserResponse(final Long roomId, final String userUser) {
        this.roomId = roomId;
        this.userUser = userUser;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getUserUser() {
        return userUser;
    }
}
