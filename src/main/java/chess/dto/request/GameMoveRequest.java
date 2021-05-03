package chess.dto.request;

public class GameMoveRequest {
    Long roomId;
    String from;
    String to;

    public GameMoveRequest(Long roomId, String from, String to) {
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
