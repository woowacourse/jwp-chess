package chess.dto;

public class RoomDto {
    private final int gameId;
    private final String name;

    public RoomDto(int gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public int getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }
}
