package chess.domain.piece;

import java.util.List;

import chess.domain.position.Direction;
import chess.domain.position.Position;

public class Bishop extends StraightMovablePiece {

    public static final List<Position> BLACK_INIT_LOCATIONS = List.of(
        Position.of("c8"), Position.of("f8"));
    public static final List<Position> WHITE_INIT_LOCATIONS = List.of(
        Position.of("c1"), Position.of("f1"));

    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    protected List<Direction> getMovableDirections() {
        return Direction.bishopDirections();
    }
}
