package chess.dto.response;

public class MoveResponseDto {
    private final boolean isMovable;
    private final String errorMessage;

    public MoveResponseDto(final boolean isMovable) {
        this(isMovable, null);
    }

    public MoveResponseDto(final boolean isMovable, final String errorMessage) {
        this.isMovable = isMovable;
        this.errorMessage = errorMessage;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
