package chess.dto.request;

public class PiecesRequestDto {

    private final int roomId;
    private String source;
    private String target;

    public PiecesRequestDto(int roomId, String source, String target) {
        this.roomId = roomId;
        this.source = source;
        this.target = target;
    }

    public PiecesRequestDto(int roomId) {
        this.roomId = roomId;
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
