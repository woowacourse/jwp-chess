package springchess.model.board;

import springchess.model.piece.Team;
import springchess.model.status.Status;

public class Board {

    private final int id;
    private final Status status;
    private final Team team;

    public Board(Status status, Team team) {
        this(0, status, team);
    }

    public Board(int id, Status status, Team team) {
        this.id = id;
        this.status = status;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Team getTeam() {
        return team;
    }
}
