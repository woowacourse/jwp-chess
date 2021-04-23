package chess.dto;

public class ScoreDto {
    private String whiteScore;
    private String blackScore;

    public ScoreDto(String whiteScore, String blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public String getWhiteScore() {
        return whiteScore;
    }

    public String getBlackScore() {
        return blackScore;
    }
}
