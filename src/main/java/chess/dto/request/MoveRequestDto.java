package chess.dto.request;

public class MoveRequestDto {
    private final String source;
    private final String target;
    private final int roomId;

    public MoveRequestDto(final String source, final String target, final int roomId) {
        this.source = source;
        this.target = target;
        this.roomId = roomId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public int getRoomId() {
        return roomId;
    }
}
