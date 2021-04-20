package chess.domain.piece.direction;

public class Nothing extends Direction {

    public Nothing() {
        super(0, 0);
    }

    @Override
    public boolean isNothing() {
        return true;
    }
}
