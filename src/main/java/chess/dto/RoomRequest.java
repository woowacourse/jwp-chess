package chess.dto;

public class RoomRequest {

    private final String roomName;
    private final String password;

    public RoomRequest(String roomName, String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }
}
