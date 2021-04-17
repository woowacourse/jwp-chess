package chess.spring.controller.dto.response;

public class MoveResponseDto {
    private boolean isMoveError;
    private String errorMessage;

    public MoveResponseDto(boolean isMoveError, String errorMessage) {
        this.isMoveError = isMoveError;
        this.errorMessage = errorMessage;
    }

    public MoveResponseDto(boolean isMoveError) {
        this(isMoveError, null);
    }

    public boolean isMoveError() {
        return isMoveError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
