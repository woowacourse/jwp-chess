package chess.domain.piece;

import static chess.domain.piece.movementcondition.BaseMovementCondition.IMPOSSIBLE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.MUST_OBSTACLE_FREE;
import chess.domain.piece.movementcondition.MovementCondition;
import chess.domain.position.Position;
import java.math.BigDecimal;

public class Bishop extends Piece {
    private static final BigDecimal BISHOP_POINT = new BigDecimal("3");

    public Bishop(Color color) {
        super(Name.BISHOP, color);
    }

    @Override
    public MovementCondition identifyMovementCondition(Position from, Position to) {
        if (from.isDiagonalWay(to)) {
            return MUST_OBSTACLE_FREE;
        }
        return IMPOSSIBLE;
    }

    @Override
    public BigDecimal getPoint() {
        return BISHOP_POINT;
    }

}
