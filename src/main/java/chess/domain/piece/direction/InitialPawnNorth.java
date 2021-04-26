package chess.domain.piece.direction;

public class InitialPawnNorth extends Direction {

    protected InitialPawnNorth() {
        super(0, 2);
    }

    @Override
    public boolean isInitialPawnNorth() {
        return true;
    }
}
