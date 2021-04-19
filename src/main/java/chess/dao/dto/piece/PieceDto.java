package chess.dao.dto.piece;

public class PieceDto {

    private final String symbol;
    private final String position;

    public PieceDto(final String symbol, final String position) {
        this.symbol = symbol;
        this.position = position;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPosition() {
        return position;
    }
}
