package chess.dto.request;

import javax.validation.constraints.Size;

public class RoomCreateRequest {
    @Size(min = 2, max = 8, message = "방 이름은 2글자 이상 8글자 이하 입니다.")
    private String roomName;
    @Size(min = 2, max = 8, message = "방 비밀번호는 2글자 이상 8글자 이하 입니다.")
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
