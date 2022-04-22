package chess.dto;

public class ResponseDto {

    private final int statusCode;
    private final String message;

    public ResponseDto(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"statusCode\" : " + statusCode +
                ", \"message\" : \"" + message + "\"}";
    }
}
