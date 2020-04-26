package wooteco.chess.dto;

public class GameStatusDTO {
    private final double whiteScore;
    private final double blackScore;

    public GameStatusDTO(final double whiteScore, final double blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }
}
