package chess.domain.dto;

public class ScoreDto {
    private String whiteScore;
    private String blackScore;

    public ScoreDto() {
    }

    public ScoreDto(Double whiteScore, Double blackScore) {
        this.whiteScore = Double.toString(whiteScore);
        this.blackScore = Double.toString(blackScore);
    }

    public String getWhiteScore() {
        return whiteScore;
    }

    public String getBlackScore() {
        return blackScore;
    }
}
