package chess.dto.request;

import javax.validation.constraints.Size;

public class RoomCreateRequest {
    @Size(min = 2, max = 8)
    private String roomName;
    @Size(min = 4, max = 8)
    private String roomPw;
    private String userName;

    public RoomCreateRequest(final String roomName, final String roomPw, final String userName) {
        this.roomName = roomName;
        this.roomPw = roomPw;
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomPw() {
        return roomPw;
    }

    public String getUserName() {
        return userName;
    }
}
