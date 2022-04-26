package chess.dto;

public class RoomDto {

    private final int id;
    private final String roomName;
    private final String password;

    public RoomDto(int id, String roomName, String password) {
        this.id = id;
        this.roomName = roomName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }
}
