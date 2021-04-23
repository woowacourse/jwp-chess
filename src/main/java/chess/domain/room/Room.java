package chess.domain.room;

public class Room {
    private final long roomId;
    private final String title;

    public Room(long roomId, String title) {
        this.roomId = roomId;
        this.title = title;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }
}
