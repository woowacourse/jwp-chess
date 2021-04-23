package chess.controller.dto;

public class MoveDto {
    private final Long roomId;
    private final String from;
    private final String to;

    public MoveDto(Long roomId, String from, String to) {
        this.roomId = roomId;
        this.from = from;
        this.to = to;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
