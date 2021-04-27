package chess.domain.response;

public class ErrorResponse implements ChessResponse {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
