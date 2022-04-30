package chess.controller.dto.request;

public class ChessGamePasswordRequest {

    private String password;

    private ChessGamePasswordRequest() {
    }

    public ChessGamePasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
