package chess.dto;

public class ChessGameDto {
    private String gameID;
    private String password;

    public ChessGameDto(String gameID, String password) {
        this.gameID = gameID;
        this.password = password;
    }

    public String getGameID() {
        return gameID;
    }

    public String getPassword() {
        return password;
    }
}
