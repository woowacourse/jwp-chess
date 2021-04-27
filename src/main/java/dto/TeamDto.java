package dto;

import chess.domain.team.Team;

public class TeamDto {
    private PiecesDto pieces;
    private String name;
    private double score;
    private boolean isTurn;
    private String player;


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

    public TeamDto(Team team, String player) {
        this.pieces = PiecesDto.of(team.getPiecePosition(), team.getName());
        this.name = team.getName();
        this.score = team.calculateTotalScore();
        this.isTurn = team.isCurrentTurn();
        this.player = player;
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

    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "TeamDto{" +
                "pieces=" + pieces +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", isTurn=" + isTurn +
                ", player='" + player + '\'' +
                '}';
    }
}
