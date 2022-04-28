package chess.dto.response;

import chess.domain.Score;

public class ScoreResponseDto {

    private double whiteScore;
    private double blackScore;

    public ScoreResponseDto() {
    }

    public ScoreResponseDto(final double whiteScore, final double blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static ScoreResponseDto from(final Score score) {
        return new ScoreResponseDto(score.getWhiteScore(), score.getBlackScore());
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }
}
