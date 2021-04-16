package chess.domain.dto;

public class ScoreDto {
    private double whiteScore;
    private double blackScore;

    public ScoreDto() {
    }

    private ScoreDto(double whiteScore, double blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static ScoreDto of(double whiteScore, double blackScore) {
        return new ScoreDto(whiteScore, blackScore);
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }
}
