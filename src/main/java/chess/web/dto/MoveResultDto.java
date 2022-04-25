package chess.web.dto;

public class MoveResultDto {
    private boolean isMovable;
    private boolean isGameOver;
    private String winner;

    public MoveResultDto() {
        isMovable = true;
        isGameOver = false;
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

    public void setIsMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }

    public void setIsGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
