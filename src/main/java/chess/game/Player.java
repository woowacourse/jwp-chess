package chess.game;

public enum Player {

    BLACK("Black"),
    WHITE("White"),
    NONE("None"),
    ;

    private final String name;

    Player(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Player getOpponentPlayer() {
        if (this == WHITE) {
            return BLACK;
        }
        if (this == BLACK) {
            return WHITE;
        }
        return NONE;
    }
}
