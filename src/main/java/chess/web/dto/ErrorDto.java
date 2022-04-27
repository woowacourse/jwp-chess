package chess.web.dto;

public class ErrorDto {
    private final String errorMessage;

    private ErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static Object of(String errorMessage) {
        return new ErrorDto(errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
