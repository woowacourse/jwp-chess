package chess.dto;

public class MovePositionDto {

    private final long roomId;
    private final String current;
    private final String destination;

    public MovePositionDto(long roomId, String current, String destination) {
        this.roomId = roomId;
        this.current = current;
        this.destination = destination;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getCurrent() {
        return current;
    }

    public String getDestination() {
        return destination;
    }
}
