package chess.controller.web.dto.piece;

public class PieceResponseDto {

    private String symbol;
    private String position;

    public PieceResponseDto() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
