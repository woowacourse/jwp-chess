package chess.dto;

public final class MoveDTO {
    private String roomId;
    private String startPoint;
    private String endPoint;

    public MoveDTO() {
    }

    public MoveDTO(final String roomId, final String startPoint, final String endPoint) {
        this.roomId = roomId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
