package chess.dto;

public class ChessGameDto {
    private String gameID;
    private String password;

    public ChessGameDto(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public String getPassword() {
        return password;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
