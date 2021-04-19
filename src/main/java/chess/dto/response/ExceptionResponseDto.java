package chess.dto.response;

public class ExceptionResponseDto {
    private final String exceptionMessage;

    public ExceptionResponseDto(final String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
