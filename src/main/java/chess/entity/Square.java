package chess.entity;

public class Square {

    private Long roomId;
    private String position;
    private String symbol;
    private String color;

    public Square(Long roomId, String position, String symbol, String color) {
        this.roomId = roomId;
        this.position = position;
        this.symbol = symbol;
        this.color = color;
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
                "roomId=" + roomId +
                ", position='" + position + '\'' +
                ", symbol='" + symbol + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
