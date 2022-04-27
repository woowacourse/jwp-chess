package chess.entity;

public class Board {
    private final Long id;
    private String position;
    private String symbol;
    private final Long roomId;

    public Board(Long id, String position, String symbol, Long roomId) {
        this.id = id;
        this.position = position;
        this.symbol = symbol;
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }

    public Long getRoomId() {
        return roomId;
    }
}
