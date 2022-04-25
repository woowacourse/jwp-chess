package chess.controller.dto.response;

public class EndResponse {

    private final String message;

    public EndResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
