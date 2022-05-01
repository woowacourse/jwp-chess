package chess.web.dto.board;

public class GameOverDto {

    private final boolean isGameOver;

    public GameOverDto(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }
}
