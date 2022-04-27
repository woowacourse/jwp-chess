package chess.domain.position;

import chess.domain.piece.property.Team;

public final class Movement {

    private String gameId;
    private final Position source;
    private final Position target;
    private Team team;

    public Movement(final Position source, final Position target) {
        this.source = source;
        this.target = target;
    }

    public Movement(final Position source, final Position target, final String gameId, final Team team) {
        this.source = source;
        this.target = target;
        this.gameId = gameId;
        this.team = team;
    }

    public String getGameId() {
        return gameId;
    }

    public String getSource() {
        return source.toString();
    }

    public String getTarget() {
        return target.toString();
    }

    public Team getTeam() {
        return team;
    }

}
