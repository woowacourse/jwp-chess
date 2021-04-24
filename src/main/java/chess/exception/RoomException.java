package chess.exception;

public class RoomException extends RuntimeException {
    private final String roomId;

    public RoomException(final String roomId, final String message) {
        super(message);
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
