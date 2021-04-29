package chess.dto;

public class GameEntryDto {
    private final long gameId;
    private final String title;

    public GameEntryDto(long gameId, String title) {
        this.gameId = gameId;
        this.title = title;
    }

    public long getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }
}
