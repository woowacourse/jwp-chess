package chess.domain.piece;

import static chess.domain.piece.movementcondition.BaseMovementCondition.IMPOSSIBLE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.POSSIBLE;

import chess.domain.position.Position;
import chess.domain.piece.movementcondition.MovementCondition;
import java.math.BigDecimal;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public MovementCondition identifyMovementCondition(Position from, Position to) {
        if (from.isAdjacent(to)) {
            return POSSIBLE;
        }
        return IMPOSSIBLE;
    }

    @Override
    public BigDecimal getPoint() {
        return BigDecimal.ZERO;
    }
}
