package dto;

import chess.domain.team.Team;

public class TeamDto {
    private final PiecesDto pieces;
    private final String name;
    private final double score;
    private final boolean isTurn;


    public TeamDto(PiecesDto pieces, String name, double score, boolean isTurn) {
        this.pieces = pieces;
        this.name = name;
        this.score = score;
        this.isTurn = isTurn;
    }

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
