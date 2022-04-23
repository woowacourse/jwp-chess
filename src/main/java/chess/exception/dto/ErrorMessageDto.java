package chess.exception.dto;

public class ErrorMessageDto {

    private final String message;

    public ErrorMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
