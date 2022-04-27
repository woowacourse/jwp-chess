package chess.controller.dto.request;

public class GameAccessRequest {

    private int gameId;
    private String password;

    public GameAccessRequest() {

    }

    public GameAccessRequest(int gameId, String password) {
        this.gameId = gameId;
        this.password = password;
    }

    public int getGameId() {
        return gameId;
    }

    public String getPassword() {
        return password;
    }
}
