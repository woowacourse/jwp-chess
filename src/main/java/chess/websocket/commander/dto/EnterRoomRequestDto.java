package chess.websocket.commander.dto;

public class EnterRoomRequestDto {
    private Long roomId;
    private String nickname;
    private String password;

    public EnterRoomRequestDto(Long roomId, String nickname, String password) {
        this.roomId = roomId;
        this.nickname = nickname;
        this.password = password;
    }

    public EnterRoomRequestDto() {}

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
