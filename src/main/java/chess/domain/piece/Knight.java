package chess.domain.piece;

import static chess.domain.piece.movementcondition.BaseMovementCondition.IMPOSSIBLE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.POSSIBLE;
import chess.domain.piece.movementcondition.MovementCondition;
import chess.domain.position.Position;
import java.math.BigDecimal;

public class Knight extends Piece {

    private static final BigDecimal KNIGHT_POINT = new BigDecimal("2.5");

    public Knight(Color color) {
        super(color);
    }

    @Override
    public MovementCondition identifyMovementCondition(Position from, Position to) {
        if (isPossibleMovement(from, to)) {
            return POSSIBLE;
        }
        return IMPOSSIBLE;
    }

    private boolean isPossibleMovement(Position from, Position to) {
        int horizontalDistance = from.getHorizontalDistance(to);
        int verticalDistance = from.getVerticalDistance(to);
        return (horizontalDistance == 1 && verticalDistance == 2) ||
                (horizontalDistance == 2 && verticalDistance == 1);
    }

    @Override
    public BigDecimal getPoint() {
        return KNIGHT_POINT;
    }
}
