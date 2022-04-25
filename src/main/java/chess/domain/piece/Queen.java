package chess.domain.piece;

import static chess.domain.piece.movementcondition.BaseMovementCondition.IMPOSSIBLE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.MUST_OBSTACLE_FREE;
import chess.domain.piece.movementcondition.MovementCondition;
import chess.domain.position.Position;
import java.math.BigDecimal;

public class Queen extends Piece {

    private static final BigDecimal QUEEN_POINT = new BigDecimal("9");

    public Queen(Color color) {
        super(color);
    }

    @Override
    public MovementCondition identifyMovementCondition(Position from, Position to) {
        if (isPossibleMovement(from, to)) {
            return MUST_OBSTACLE_FREE;
        }
        return IMPOSSIBLE;
    }

    private boolean isPossibleMovement(Position from, Position to) {
        return from.isDiagonalWay(to) || from.isVerticalWay(to) || from.isHorizontalWay(to);
    }

    @Override
    public BigDecimal getPoint() {
        return QUEEN_POINT;
    }
}
