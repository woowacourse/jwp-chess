package chess.dto;

public class ErrorMessageDto {
    private final String errorMessage;

    public ErrorMessageDto(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
