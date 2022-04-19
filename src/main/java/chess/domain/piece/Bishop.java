package chess.domain.piece;

import chess.domain.Color;
import chess.domain.board.Position;
import java.util.function.Consumer;

public final class Bishop extends Piece {
    private static final int SCORE = 3;

    public Bishop(Color color) {
        super(color, Type.BISHOP);
    }

    @Override
    public void move(Position beforePosition, Position afterPosition, Consumer<Piece> moveFunction) {
        if (!canMove(beforePosition, afterPosition)) {
            throw new IllegalArgumentException(INVALID_TARGET_POSITION_EXCEPTION);
        }
        moveFunction.accept(this);
    }

    @Override
    public void capture(Position beforePosition, Position afterPosition, Consumer<Piece> moveFunction) {
        this.move(beforePosition, afterPosition, moveFunction);
    }

    @Override
    protected boolean canMove(Position beforePosition, Position afterPosition) {
        int columnDistance = beforePosition.columnDistance(afterPosition);
        int rowDistance = beforePosition.rowDistance(afterPosition);
        return columnDistance == rowDistance;
    }

    @Override
    public double getScore() {
        return SCORE;
    }

    @Override
    public String getSymbol() {
        return "b";
    }
}
