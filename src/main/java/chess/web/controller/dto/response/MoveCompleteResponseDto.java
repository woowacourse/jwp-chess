package chess.web.controller.dto.response;

public class MoveCompleteResponseDto {

    private final boolean canMove;

    public MoveCompleteResponseDto() {
        canMove = true;
    }

    public boolean isCanMove() {
        return canMove;
    }
}
