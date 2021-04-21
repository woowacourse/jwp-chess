package chess.dto;

public class ScoreDTO {
    private String whiteScore;
    private String blackScore;

    public ScoreDTO(String whiteScore, String blackScore) {
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
