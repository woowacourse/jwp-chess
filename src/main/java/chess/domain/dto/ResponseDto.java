package chess.domain.dto;

public class ResponseDto {
    private final String message;

    public ResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
