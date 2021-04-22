package chess.web.controller.dto.response;

public class MoveErrorResponseDto {

    private final boolean canMove;
    private final String errorMessage;

    public MoveErrorResponseDto(IllegalArgumentException e) {
        canMove = false;
        errorMessage = e.getMessage();
    }

    public boolean isCanMove() {
        return canMove;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
