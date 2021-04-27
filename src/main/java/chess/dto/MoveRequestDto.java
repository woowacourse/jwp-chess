package chess.dto;

public class MoveRequestDto {

    String source;
    String target;
    String roomName;

    public MoveRequestDto() {
    }

    public MoveRequestDto(String source, String target, String roomName) {
        this.source = source;
        this.target = target;
        this.roomName = roomName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
