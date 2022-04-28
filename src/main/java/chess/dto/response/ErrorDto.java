package chess.dto.response;

public class ErrorDto {
    private final String message;

    public ErrorDto(String message) {
        this.message = message;
    }

    public boolean getError() {
        return true;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "message='" + message + '\'' +
                '}';
    }
}
