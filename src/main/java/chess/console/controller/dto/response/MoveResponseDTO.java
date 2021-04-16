package chess.console.controller.dto.response;

public class MoveResponseDTO {
    private boolean isMoveError;
    private String errorMessage;

    public MoveResponseDTO() {
    }

    public MoveResponseDTO(boolean isMoveError, String errorMessage) {
        this.isMoveError = isMoveError;
        this.errorMessage = errorMessage;
    }

    public MoveResponseDTO(boolean isMoveError) {
        this(isMoveError, null);
    }

    public boolean isMoveError() {
        return isMoveError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
