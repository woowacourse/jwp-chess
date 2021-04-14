package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.position.Target;

public final class Blank extends Piece {

    private static final String INITIAL_NAME = ".";

    public Blank(final Position position) {
        super(Color.NOTHING, INITIAL_NAME, position);
    }

    @Override
    public Piece move(Target target) {
        throw new UnsupportedOperationException("움직일 수 없습니다.");
    }

    @Override
    public boolean canMove(final Target target) {
        throw new UnsupportedOperationException("비어 있는 칸입니다.");
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isBlank() {
        return true;
    }

    @Override
    public boolean isKing() {
        return false;
    }
}
