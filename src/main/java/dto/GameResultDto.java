package dto;

import chess.domain.game.GameResult;
import chess.domain.game.ScoreCalculator;
import chess.domain.piece.Color;
import chess.domain.piece.Pieces;

public class GameResultDto {

    private final Color winner;

    private final double whiteScore;
    private final double blackScore;
    public GameResultDto(Color winner, double whiteScore, double blackScore) {
        this.winner = winner;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static GameResultDto of(GameResult gameResult) {
        return new GameResultDto(gameResult.getWinner(), gameResult.getWhiteScore(), gameResult.getBlackScore());
    }

    public Color getWinner() {
        return winner;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }
}
