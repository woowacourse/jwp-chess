package chess.entity;

public class BoardEntity {

    private final long id;
    private final String position;
    private final String symbol;
    private final long roomId;

    public BoardEntity(long id, String position, String symbol, long roomId) {
        this.id = id;
        this.position = position;
        this.symbol = symbol;
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }

    public long getRoomId() {
        return roomId;
    }
}
