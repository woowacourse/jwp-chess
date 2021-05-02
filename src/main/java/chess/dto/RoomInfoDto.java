package chess.dto;

public class RoomInfoDto {
    private final String name;
    private final String pw;
    private final Long gameId;


    public RoomInfoDto(final String name, final String pw, final Long gameId) {
        this.name = name;
        this.pw = pw;
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }
}
