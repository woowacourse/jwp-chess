package chess.domain;

public class GameScore {
    private final Score blackScore;
    private final Score whiteScore;

    public GameScore(Score blackScore, Score whiteScore) {
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public Score getBlackScore() {
        return blackScore;
    }

    public Score getWhiteScore() {
        return whiteScore;
    }
}
