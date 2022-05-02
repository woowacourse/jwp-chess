package chess.dto;

public class ChessGameRequest {
    private String gameId;
    private String password;

    public ChessGameRequest(String gameId, String password) {
        this.gameId = gameId;
        this.password = password;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPassword() {
        return password;
    }
}
