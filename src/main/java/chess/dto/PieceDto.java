package chess.dto;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Symbol;

public class PieceDto {

    private String symbol;
    private String color;

    public PieceDto(String symbol, String color) {
        this.symbol = symbol;
        this.color = color;
    }

    public Piece toEntity() {
        return Piece.of(Color.valueOf(color), Symbol.valueOf(symbol));
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
