package chess.domain.dto;

public class ScoreDto {
    private String whiteScore;
    private String blackScore;

    public ScoreDto() {
    }

    private ScoreDto(Double whiteScore, Double blackScore) {
        this.whiteScore = Double.toString(whiteScore);
        this.blackScore = Double.toString(blackScore);
    }

    public static ScoreDto of(double whiteScore, double blackScore) {
        return new ScoreDto(whiteScore, blackScore);
    }

    public String getWhiteScore() {
        return whiteScore;
    }

    public String getBlackScore() {
        return blackScore;
    }
}
