package chess.controller.dto;

import chess.entity.ChessPieceEntity;

public class ChessPieceResponse {

    private final String position;
    private final String pieceType;
    private final String color;

    public ChessPieceResponse(ChessPieceEntity chessPieceEntity) {
        this.position = chessPieceEntity.getPosition();
        this.pieceType = chessPieceEntity.getChessPiece();
        this.color = chessPieceEntity.getColor();
    }

    public String getPosition() {
        return position;
    }

    public String getPieceType() {
        return pieceType;
    }

    public String getColor() {
        return color;
    }
}
