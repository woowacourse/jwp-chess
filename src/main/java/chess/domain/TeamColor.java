package chess.domain;

public enum TeamColor {
    WHITE, BLACK, NONE;

    public static TeamColor teamColor(String teamColor) {
        if (teamColor.equalsIgnoreCase("WHITE")) {
            return WHITE;
        }

        if (teamColor.equalsIgnoreCase("BLACK")) {
            return BLACK;
        }
        return NONE;
    }

    public TeamColor reverse() {
        if (this == WHITE) {
            return BLACK;
        }

        if (this == BLACK) {
            return WHITE;
        }
        return this;
    }

    public boolean isWhite() {
        return this == WHITE;
    }

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isEnemy(TeamColor teamColor) {
        return (teamColor.isWhite() && this.isBlack()) || (teamColor.isBlack() && this.isWhite());
    }

}
