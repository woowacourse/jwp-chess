package wooteco.chess.dto;

public class GameStatusDto {
    private final double whiteScore;
    private final double blackScore;

    public GameStatusDto(final double whiteScore, final double blackScore) {
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
