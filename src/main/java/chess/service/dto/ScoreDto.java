package chess.service.dto;

public class ScoreDto {

    private double whiteScore;
    private double blackScore;

    public ScoreDto(){}

    public ScoreDto(final double whiteScore, final double blackScore) {
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
