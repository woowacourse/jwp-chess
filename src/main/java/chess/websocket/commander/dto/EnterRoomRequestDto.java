package chess.websocket.commander.dto;

public class EnterRoomRequestDto {
    private Long roomId;
    private String password;

    public EnterRoomRequestDto(Long roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
