package chess.dto.response;

public class MoveResponseDto {
    private final boolean isMoveError;
    private final String errorMessage;

    public MoveResponseDto(final boolean isMoveError) {
        this(isMoveError, null);
    }

    public MoveResponseDto(final boolean isMoveError, final String errorMessage) {
        this.isMoveError = isMoveError;
        this.errorMessage = errorMessage;
    }

    public boolean isMoveError() {
        return isMoveError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
