package chess.dto;

import chess.domain.Team;

public class RoomDto {
    private final Long id;
    private final Team status;
    private final String name;
    private final String password;

    public RoomDto(Long id, Team status, String name, String password) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Team getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
