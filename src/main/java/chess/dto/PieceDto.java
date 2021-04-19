package chess.dto;

import chess.domain.piece.Piece;

public class PieceDto {
    private String name;
    private String team;

    public PieceDto(Piece piece) {
        if (piece != null) {
            this.name = piece.getName();
            this.team = piece.getTeam().name();
        }
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }
}
