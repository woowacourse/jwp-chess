package chess.dto;

public class MovablePositionDto {
    private String roomId;
    private String target;

    public MovablePositionDto(String roomId, String target) {
        this.roomId = roomId;
        this.target = target;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getTarget() {
        return target;
    }
}
