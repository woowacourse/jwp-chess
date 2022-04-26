package chess.web.dto;

public class MoveResultDto {

    private boolean isMovable;
    private boolean isGameOver;
    private String winner;

    private MoveResultDto(boolean isMovable, boolean isGameOver, String winner) {
        this.isMovable = isMovable;
        this.isGameOver = isGameOver;
        this.winner = winner;
    }

    public static MoveResultDto of(boolean isMovable, boolean isGameOver, String winner) {
        return new MoveResultDto(isMovable, isGameOver, winner);
    }

    public boolean getIsMovable() {
        return isMovable;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public String getWinner() {
        return winner;
    }
}
