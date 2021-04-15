package chess.dto;

public class MoveDTO {
    private String roomId;
    private String startPoint;
    private String endPoint;

    public MoveDTO() {
    }

    public MoveDTO(String roomId, String startPoint, String endPoint) {
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
