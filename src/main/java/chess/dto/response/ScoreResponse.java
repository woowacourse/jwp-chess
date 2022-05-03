package chess.dto.response;

public class ScoreResponse {
    private final float whiteScore;
    private final float blackScore;

    public ScoreResponse(float whiteScore, float blackScore) {
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
