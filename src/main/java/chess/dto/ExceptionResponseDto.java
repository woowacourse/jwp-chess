package chess.dto;

public class ExceptionResponseDto {

    private final String message;

    public ExceptionResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
