package chess.dto.response;

public class MoveResponseDto {
    private final boolean isMoveError;

    public MoveResponseDto(final boolean isMoveError) {
        this.isMoveError = isMoveError;
    }

    public boolean isMoveError() {
        return isMoveError;
    }
}
