package chess.web.dto.board;

public class IsGameOverDto {

    private final boolean isGameOver;

    public IsGameOverDto(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }
}
