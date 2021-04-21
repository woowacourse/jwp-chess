package chess.controller.dto;

import chess.domain.TeamColor;
import chess.domain.piece.Piece;

public class PieceDto {

    private final String currentPosition;
    private final String name;
    private final TeamColor teamColor;

    public PieceDto(Piece piece) {
        this.currentPosition = piece.currentPosition().columnAndRow();
        this.name = piece.name();
        this.teamColor = piece.color();
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public String getName() {
        return name;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }
}
