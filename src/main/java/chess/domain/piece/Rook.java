package chess.domain.piece;

import static chess.domain.piece.movementcondition.BaseMovementCondition.IMPOSSIBLE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.MUST_OBSTACLE_FREE;

import chess.domain.position.Position;
import chess.domain.piece.movementcondition.MovementCondition;
import java.math.BigDecimal;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
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
        return new BigDecimal("5");
    }
}
