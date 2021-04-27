package chess.dto;

public class MoveRequestDto {
    private String source;
    private String target;
    private String roomId;

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getRoomId() {
        return roomId;
    }
}
