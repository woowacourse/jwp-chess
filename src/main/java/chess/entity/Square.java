package chess.entity;

public class Square {

    private Long id;
    private Long roomId;
    private String position;
    private String symbol;
    private String color;

    public Square(Long id, Long roomId, String position, String symbol, String color) {
        this.id = id;
        this.roomId = roomId;
        this.position = position;
        this.symbol = symbol;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Square{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", position='" + position + '\'' +
                ", symbol='" + symbol + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
