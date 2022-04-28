package chess.database.dto;

public class RoomDto {
    private final Long id;
    private final String roomName;
    private final String password;

    public RoomDto(Long id, String roomName, String password) {
        this.id = id;
        this.roomName = roomName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }
}
