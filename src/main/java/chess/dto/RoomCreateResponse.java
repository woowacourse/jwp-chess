package chess.dto;

public class RoomCreateResponse {
    public Long id;

    public RoomCreateResponse(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
