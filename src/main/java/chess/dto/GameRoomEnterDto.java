package chess.dto;

public class GameRoomEnterDto {
    private final Long gameId;
    private final String password;

    public GameRoomEnterDto(final Long gameId, final String password) {
        this.gameId = gameId;
        this.password = password;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPassword() {
        return password;
    }
}
