package chess.dto;

import chess.domain.Team;
import chess.entity.Room;

public class RoomDto {

    private final Long id;
    private final Team team;
    private final String title;
    private final String password;
    private boolean status;

    public RoomDto(Long id, Team team, String title, String password, boolean status) {
        this.id = id;
        this.team = team;
        this.title = title;
        this.password = password;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public boolean isStatus() {
        return status;
    }

    public Room toRoom() {
        return new Room(id, team, title, password, status);
    }
}
