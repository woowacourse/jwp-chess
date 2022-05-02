package chess.dto;

public class ChessGameDto {
    private String gameId;
    private String password;

    public ChessGameDto(String gameId, String password) {
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
