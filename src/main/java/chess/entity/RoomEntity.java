package chess.entity;

import chess.domain.Team;

public class RoomEntity {

    private final long id;
    private final Team status;
    private final String name;
    private final String password;

    public RoomEntity(long id, Team status, String name, String password) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.password = password;
    }

    public long getId() {
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
