package chess.web.dto.board;

public class MoveResultDto {

    private final boolean isGameOver;

    public MoveResultDto(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }
}
