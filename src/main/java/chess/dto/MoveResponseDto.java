package chess.dto;

public class MoveResponseDto {

    private final boolean finished;
    private final boolean success;
    private final String turn;
    private final double blackScore;
    private final double whiteScore;

    public MoveResponseDto(boolean finished, boolean success, String turn, double blackScore, double whiteScore) {
        this.finished = finished;
        this.success = success;
        this.turn = turn;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTurn() {
        return turn;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

}
