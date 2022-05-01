package chess.domain.room;

public class RoomName {

    private final String roomName;

    private RoomName(String roomName) {
        this.roomName = roomName;
    }

    public static RoomName of(String name) {
        return new RoomName(name);
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        return roomName;
    }
}
