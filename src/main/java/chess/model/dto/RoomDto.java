package chess.model.dto;

public class RoomDto {
    private final String roomName;
    private String password;

    public RoomDto(String roomName, String password) {
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
