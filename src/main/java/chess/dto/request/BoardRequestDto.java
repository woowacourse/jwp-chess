package chess.dto.request;

public class BoardRequestDto {

    private int roomId;
    private String source;
    private String target;

    public int getRoomId() {
        return roomId;
    }

    public BoardRequestDto(int roomId, String source, String target) {
        this.roomId = roomId;
        this.source = source;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public String getSource() {
        return source;
    }

}
