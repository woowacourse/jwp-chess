package chess.domain;

public class GameEntry {
    private String gameID;
    private String gameCode;

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameID() {
        return gameID;
    }

    public String getGameCode() {
        return gameCode;
    }
}
