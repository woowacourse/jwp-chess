package chess.dto;

import java.io.Serializable;

public class MoveDTO implements Serializable {

    private String source;
    private String target;
    private String team;

    public MoveDTO(String source, String target, String team) {
        this.source = source;
        this.target = target;
        this.team = team;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getTeam() {
        return team;
    }
}
