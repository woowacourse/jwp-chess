package chess.dto;

import chess.domain.piece.Piece;

public class PieceDto {
    private final String name;
    private final String color;

    private PieceDto(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static PieceDto from(Piece piece){
        return new PieceDto(piece.getName(), piece.getColor().name());
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "PieceDto{" +
                "notation='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
