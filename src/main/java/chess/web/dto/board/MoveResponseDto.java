package chess.web.dto.board;

public class MoveResponseDto {

    private final boolean isGameOver;

    public MoveResponseDto(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }
}
