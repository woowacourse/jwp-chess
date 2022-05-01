package chess.dto.response;

import chess.domain.Team;

public class RoomDto {

    private final Long id;
    private final Team team;
    private final String title;
    private boolean status;

    public RoomDto(Long id, Team team, String title, boolean status) {
        this.id = id;
        this.team = team;
        this.title = title;
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

    public boolean isStatus() {
        return status;
    }

}
