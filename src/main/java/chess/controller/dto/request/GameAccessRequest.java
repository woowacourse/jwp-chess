package chess.controller.dto.request;

public class GameAccessRequest {

    private long gameId;
    private String password;

    public GameAccessRequest() {

    }

    public GameAccessRequest(long gameId, String password) {
        this.gameId = gameId;
        this.password = password;
    }

    public long getGameId() {
        return gameId;
    }

    public String getPassword() {
        return password;
    }
}
