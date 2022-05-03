package chess.dto;

public class GameCreateResponse {

    private long id;

    public GameCreateResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
