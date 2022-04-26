package chess.dto;

import chess.domain.piece.Piece;

public class PieceDto {

    private String position;
    private String color;
    private String name;

    public PieceDto() {
    }

    public PieceDto(Piece piece) {
        this.position = piece.getPosition().getPosition();
        this.color = piece.getColor().getName();
        this.name = piece.getName();
    }

    public PieceDto(String position, String color, String name) {
        this.position = position;
        this.color = color;
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

}
