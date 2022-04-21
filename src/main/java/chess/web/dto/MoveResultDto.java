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

    public void setCanMove(boolean canMove) {
        this.isMovable = canMove;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
