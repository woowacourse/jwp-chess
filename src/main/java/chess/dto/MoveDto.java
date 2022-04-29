package chess.dto;

public class MoveDto {

    private int roomId;
    private String from;
    private String to;

    public MoveDto() {
    }

    public MoveDto(int roomId, String from, String to) {
        this.roomId = roomId;
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getRoomId() {
        return roomId;
    }
}
