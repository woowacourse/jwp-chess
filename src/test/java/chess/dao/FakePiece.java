package chess.dao;

public class FakePiece {

    private final String position;
    private final String symbol;
    private final long roomId;

    public FakePiece(String position, String symbol, long roomId) {
        this.position = position;
        this.symbol = symbol;
        this.roomId = roomId;
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
