package chess.dto;

public class ScoreDto {
    private final float whiteScore;
    private final float blackScore;

    public ScoreDto(float whiteScore, float blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public float getWhiteScore() {
        return whiteScore;
    }

    public float getBlackScore() {
        return blackScore;
    }
}
