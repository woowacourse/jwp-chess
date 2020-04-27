package spring.chess.team;

public enum Team {
    BLACK(1),
    WHITE(0);

    private int turnIsBlack;

    Team(int turnIsBlack) {
        this.turnIsBlack = turnIsBlack;
    }

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isReverseTeam(Team team) {
        return this != team;
    }

    public Team changeTurn() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }

    public static Team of(boolean isTurnBlack) {
        if (isTurnBlack) {
            return BLACK;
        }
        return WHITE;
    }

    public int getTurnIsBlack() {
        return turnIsBlack;
    }
}
