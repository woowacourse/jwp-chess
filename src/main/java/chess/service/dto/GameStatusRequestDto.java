package chess.service.dto;

public class GameStatusRequestDto {

    private String chessName;
    private boolean isGameOver;

    public GameStatusRequestDto(boolean isGameOver, String chessName) {
        this.isGameOver = isGameOver;
        this.chessName = chessName;
    }

    public String getChessName() {
        return chessName;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
