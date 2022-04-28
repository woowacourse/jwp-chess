package chess.dto;

import chess.domain.Team;

public class RoomStatusDto {
    private final Long id;
    private final Team status;

    public RoomStatusDto(Long id, Team status) {
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
