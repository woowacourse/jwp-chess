package chess.database.entity;

public class RoomEntity {

    private Long id;
    private final String roomName;
    private final String password;

    public RoomEntity(Long id, String roomName, String password) {
        this.id = id;
        this.roomName = roomName;
        this.password = password;
    }

    private RoomEntity(String roomName, String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public static RoomEntity from(String roomName, String password) {
        return new RoomEntity(roomName, password);
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
