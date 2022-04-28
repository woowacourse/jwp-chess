package chess.dto.response;

import chess.domain.Team;

public class RoomStatusResponse {
    private final Long id;
    private final Team status;

    public RoomStatusResponse(Long id, Team status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Team getStatus() {
        return status;
    }
}
