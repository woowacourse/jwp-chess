package chess.web.dto;

public class MoveResultDto {

    private boolean isGameOver;
    private String winner;

    private MoveResultDto(boolean isGameOver, String winner) {
        this.isGameOver = isGameOver;
        this.winner = winner;
    }

    public static MoveResultDto of(boolean isGameOver, String winner) {
        return new MoveResultDto(isGameOver, winner);
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public String getWinner() {
        return winner;
    }
}
