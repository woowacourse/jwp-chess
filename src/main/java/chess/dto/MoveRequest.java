package chess.dto;

public final class MoveRequest {

    private final String source;
    private final String target;
    private final String team;

    public MoveRequest(final String source, final String target, final String team) {
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
