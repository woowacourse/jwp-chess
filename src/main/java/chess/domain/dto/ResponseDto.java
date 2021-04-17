package chess.domain.dto;

public class ResponseDto {
    private final String code;
    private final String message;

    public ResponseDto(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
