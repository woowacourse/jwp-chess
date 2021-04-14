package chess.domain.piece;

public final class Blank extends Piece {

    private static final Piece BLANK = new Blank();
    private static final String INITIAL_NAME = ".";

    private Blank() {
        super(Team.NOTHING, INITIAL_NAME);
    }

    public static Piece getInstance() {
        return BLANK;
    }
}
