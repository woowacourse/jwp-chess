package chess.dto;

public class GameResultDto {

    private String winner;
    private double whiteScore;
    private double blackScore;

    public GameResultDto() {
    }

    public GameResultDto(String winner, double whiteScore, double blackScore) {
        this.winner = winner;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public String getWinner() {
        return winner;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

}
