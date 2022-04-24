package chess.dto.response;

import chess.domain.game.score.Score;

public class ScoreResultDto {
    private final double whiteScore;
    private final double blackScore;

    private ScoreResultDto(double whiteScore, double blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static ScoreResultDto of(Score whiteScore, Score blackScore) {
        return new ScoreResultDto(whiteScore.getValue(), blackScore.getValue());
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

    @Override
    public String toString() {
        return "ScoreResultDto{" +
                "whiteScore=" + whiteScore +
                ", blackScore=" + blackScore +
                '}';
    }
}
