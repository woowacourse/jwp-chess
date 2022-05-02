package chess.model.piece;

import static chess.model.piece.strategy.move.Direction.NORTHEAST;
import static chess.model.piece.strategy.move.Direction.NORTHWEST;
import static chess.model.piece.strategy.move.Direction.SOUTHEAST;
import static chess.model.piece.strategy.move.Direction.SOUTHWEST;

import chess.model.Color;
import chess.model.piece.strategy.UnlimitedMovableStrategy;
import chess.model.piece.strategy.move.Direction;
import java.util.List;

public final class Bishop extends Piece {

    public static final List<Direction> BISHOP_DIRECTION = List.of(SOUTHEAST, NORTHEAST, SOUTHWEST, NORTHWEST);

    public Bishop(Color color) {
        super(color, new UnlimitedMovableStrategy(BISHOP_DIRECTION));
    }

    @Override
    public double getPointValue() {
        return Point.BISHOP.getValue();
    }
}
