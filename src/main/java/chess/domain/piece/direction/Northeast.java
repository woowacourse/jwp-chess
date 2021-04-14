package chess.domain.piece.direction;

public class Northeast extends Direction {

    public Northeast() {
        super(1, 1);
    }

    @Override
    public boolean isNorthEast() {
        return true;
    }
}
