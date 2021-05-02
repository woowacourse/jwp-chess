package chess.dto;

import chess.domain.team.Team;

public class TeamDto {
    private PiecesDto pieces;
    private String name;
    private double score;
    private boolean isTurn;

    public TeamDto(Team team) {
        this.pieces = PiecesDto.of(team.getPiecePosition(), team.getName());
        this.name = team.getName();
        this.score = team.calculateTotalScore();
        this.isTurn = team.isCurrentTurn();
    }

    public PiecesDto getPieces() {
        return pieces;
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public boolean isTurn() {
        return isTurn;
    }
}
