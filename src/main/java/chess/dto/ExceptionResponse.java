package chess.dto;

public class ExceptionResponse {
    private final boolean ok;
    private final String message;

    public ExceptionResponse(String message) {
        this.ok = false;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}
