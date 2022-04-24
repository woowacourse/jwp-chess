package chess.domain.dto;

public class ResponseDto1 {
    private final int status;
    private final String message;

    public ResponseDto1(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String convertToString() {
        return "{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
