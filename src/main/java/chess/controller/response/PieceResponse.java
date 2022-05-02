package chess.controller.response;

import chess.serviece.dto.PieceDto;

public class PieceResponse {

    private final String position;
    private final String color;
    private final String type;

    public PieceResponse(String position, String color, String type) {
        this.position = position;
        this.color = color;
        this.type = type;
    }

    public static PieceResponse from(PieceDto pieceDto) {
        return new PieceResponse(pieceDto.getPosition(), pieceDto.getColor(), pieceDto.getType());
    }

    public String getPosition() {
        return position;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
