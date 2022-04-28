package chess.dto;

public class GameDeleteResponse {

    private boolean ok;
    private String message;

    public GameDeleteResponse() {
    }

    public GameDeleteResponse(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}
