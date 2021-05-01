package chess.dto;

public class RoomInfoRequestDto {
    String roomName;
    String password;

    public RoomInfoRequestDto(String roomName, String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
