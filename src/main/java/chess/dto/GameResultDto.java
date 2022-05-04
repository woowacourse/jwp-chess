package chess.dto;

import chess.domain.game.GameResult;

public class GameResultDto {

    private String winner;
    private double whiteScore;
    private double blackScore;

    public GameResultDto() {
    }

    private GameResultDto(String winner, double whiteScore, double blackScore) {
        this.winner = winner;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static GameResultDto toDto(GameResult gameResult) {
        return new GameResultDto(gameResult.getWinner().getName(), gameResult.getWhiteScore(), gameResult.getBlackScore());
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
