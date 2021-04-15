package chess.dto;

public class PositionDTO {
    private final String from;
    private final String to;
    private final String roomName;

    public PositionDTO(String from, String to, String roomName) {
        this.from = from;
        this.to = to;
        this.roomName = roomName;
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }

    public String roomName() {
        return roomName;
    }
}
