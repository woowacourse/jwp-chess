package chess.dto.response;

public class GameRoomResponse {

    private final long id;
    private final String name;

    public GameRoomResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
