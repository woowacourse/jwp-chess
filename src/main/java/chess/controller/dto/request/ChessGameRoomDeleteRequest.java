package chess.controller.dto.request;

public class ChessGameRoomDeleteRequest {

    private String password;

    private ChessGameRoomDeleteRequest() {
    }

    public ChessGameRoomDeleteRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
