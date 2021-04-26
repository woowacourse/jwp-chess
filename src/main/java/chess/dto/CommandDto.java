package chess.dto;

public class CommandDto {
    private String roomId;
    private String target;
    private String destination;

    public CommandDto(String roomId, String target, String destination) {
        this.roomId = roomId;
        this.target = target;
        this.destination = destination;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getTarget() {
        return target;
    }

    public String getDestination() {
        return destination;
    }
}
