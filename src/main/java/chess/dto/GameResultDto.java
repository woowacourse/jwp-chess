package chess.dto;

import chess.domain.game.GameResult;

public class GameResultDto {

    private String winner;
    private double whiteScore;
    private double blackScore;

    public GameResultDto() {
    }

    public GameResultDto(String winner, double whiteScore, double blackScore) {
        this.winner = winner;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public GameResultDto(GameResult gameResult) {
        this.winner = gameResult.getWinner().getName();
        this.whiteScore = gameResult.getWhiteScore();
        this.blackScore = gameResult.getBlackScore();
    }

    public String getWinner() {
        return winner;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

}
