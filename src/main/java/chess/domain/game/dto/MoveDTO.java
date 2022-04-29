package chess.domain.game.dto;

import chess.domain.piece.property.Team;

import java.io.Serializable;

public class MoveDTO implements Serializable {
    private String source;
    private String target;
    private Team team;

    public MoveDTO(String source, String target, String team) {
        this.source = source;
        this.target = target;
        this.team = Team.valueOf(team);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = Team.valueOf(team);
    }

    @Override
    public String toString() {
        return "MoveDTO{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", team=" + team +
                '}';
    }
}
