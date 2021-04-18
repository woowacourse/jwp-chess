package chess.service.dto;

public class GameStatusRequestDto {

    private String chessName;
    private boolean isGameOver;

    public GameStatusRequestDto() {
    }

    public GameStatusRequestDto(boolean isGameOver, String chessName) {
        this.isGameOver = isGameOver;
        this.chessName = chessName;
    }

    public String getChessName() {
        return chessName;
    }

    public void setChessName(String chessName) {
        this.chessName = chessName;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
