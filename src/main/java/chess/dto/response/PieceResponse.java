package chess.dto.response;

public class PieceResponse {
    public String position;
    public String symbol;

    public PieceResponse(String position, String symbol) {
        this.position = position;
        this.symbol = symbol;
    }

    public String getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }
}
