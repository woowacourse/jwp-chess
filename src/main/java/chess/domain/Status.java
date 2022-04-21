package chess.domain;

public class Status {

    private final double whiteScore;
    private final double blackScore;

    public Status(double whiteScore, double blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }
}
