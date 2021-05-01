package chess.dto;

public class CreateExceptionResponseDto {

    private final String message;

    public CreateExceptionResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
