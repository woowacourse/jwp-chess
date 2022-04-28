package chess.dto.response;

import chess.entity.PieceEntity;

public class PieceResponseDto {

    private final String position;
    private final String type;
    private final String color;

    private PieceResponseDto(final String position, final String type, final String color) {
        this.position = position;
        this.type = type;
        this.color = color;
    }

    public static PieceResponseDto from(final PieceEntity piece) {
        return new PieceResponseDto(piece.getPosition(), piece.getType(), piece.getColor());
    }

    public String getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}
