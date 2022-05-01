package chess.domain.piece;

import static chess.domain.piece.movementcondition.BaseMovementCondition.IMPOSSIBLE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.MUST_OBSTACLE_FREE;
import chess.domain.piece.movementcondition.MovementCondition;
import chess.domain.position.Position;
import java.math.BigDecimal;

public class Rook extends Piece {
    private static final BigDecimal ROOK_POINT = new BigDecimal("5");

    public Rook(Color color) {
        super(Name.ROOK, color);
    }

    @Override
    public MovementCondition identifyMovementCondition(Position from, Position to) {
        if (from.isVerticalWay(to) || from.isHorizontalWay(to)) {
            return MUST_OBSTACLE_FREE;
        }
        return IMPOSSIBLE;
    }

    @Override
    public BigDecimal getPoint() {
        return ROOK_POINT;
    }
}
