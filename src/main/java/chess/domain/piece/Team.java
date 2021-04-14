package chess.domain.piece;


public enum Team {
    BLACK("Black"),
    WHITE("White"),
    NOTHING("");

    private final String teamName;

    Team(final String teamName) {
        this.teamName = teamName;
    }

    public boolean isBlackTeam() {
        return this == BLACK;
    }
}
