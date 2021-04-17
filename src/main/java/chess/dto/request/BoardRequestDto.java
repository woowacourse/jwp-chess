package chess.dto.request;

public class BoardRequestDto {

    private final int roomId;
    private final String source;
    private final String target;

    public BoardRequestDto(int roomId, String source, String target) {
        this.roomId = roomId;
        this.source = source;
        this.target = target;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

}
