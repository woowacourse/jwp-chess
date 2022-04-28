package chess.dto.request;

public class GameIdRequest {

    private final Long id;

    public GameIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
