package chess.web.dto.board;

public class moveResultDto {

    private final boolean isGameOver;

    public moveResultDto(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }
}
