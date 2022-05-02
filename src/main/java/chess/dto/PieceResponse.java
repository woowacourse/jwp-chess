package chess.dto;

public class PieceResponse {

    private String symbol;
    private String color;

    public PieceResponse(String symbol, String color) {
        this.symbol = symbol;
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public String getPiece() {
        return color + symbol;
    }
}
