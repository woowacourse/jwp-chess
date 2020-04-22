package wooteco.chess.controller.dto;

import wooteco.chess.domain.piece.PieceState;

public class PieceDto {

    private String pieceType;
    private String team;

    private PieceDto(String pieceType, String team) {
        this.pieceType = pieceType;
        this.team = team;
    }

    public static PieceDto of(PieceState pieceState) {
        return new PieceDto(pieceState.getPieceType().toString(), pieceState.getTeam().toString());
    }

    public String getTeam() {
        return team;
    }

    public String getPieceType() {
        return pieceType;
    }
}
