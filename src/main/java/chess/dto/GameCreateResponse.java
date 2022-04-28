package chess.dto;

public class GameCreateResponse {

    private int id;

    public GameCreateResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
