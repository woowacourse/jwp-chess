package chess;

public class ChessGameVO {
    private String gameID;
    private String password;

    public ChessGameVO(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public String getPassword() {
        return password;
    }
}
