package chess.dto.game;

public final class PieceDTO {
    private final String team;
    private final String initial;
    private final String position;

    public PieceDTO(final String team, final String initial, final String position) {
        this.team = team;
        this.initial = initial;
        this.position = position;
    }

    public String getTeam() {
        return team;
    }

    public String getInitial() {
        return initial;
    }

    public String getPosition() {
        return position;
    }
}
