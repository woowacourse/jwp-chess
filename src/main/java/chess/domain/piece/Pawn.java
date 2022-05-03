package chess.domain.piece;

import static chess.domain.piece.movementcondition.BaseMovementCondition.IMPOSSIBLE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.MUST_CAPTURE_PIECE;
import static chess.domain.piece.movementcondition.BaseMovementCondition.MUST_EMPTY_DESTINATION;
import static chess.domain.piece.movementcondition.BaseMovementCondition.MUST_OBSTACLE_FREE;
import chess.domain.piece.movementcondition.MovementCondition;
import chess.domain.piece.movementcondition.MovementConditions;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.math.BigDecimal;
import java.util.Set;

public class Pawn extends Piece {
    private static final Rank BLACK_START_RANK = Rank.SEVEN;
    private static final Rank WHITE_START_RANK = Rank.TWO;

    private static final int MOVE_DISTANCE = 1;
    private static final int START_MOVE_DISTANCE = 2;

    public Pawn(Color color) {
        super(Name.PAWN, color);
    }

    @Override
    public MovementCondition identifyMovementCondition(Position from, Position to) {
        if (isForward(from, to) && isVerticalWay(from, to) && isValidDistance(from, to)) {
            return new MovementConditions(Set.of(MUST_EMPTY_DESTINATION, MUST_OBSTACLE_FREE));
        }

        if (isForward(from, to) && isDiagonalWay(from, to) && isValidDistance(from, to)) {
            return MUST_CAPTURE_PIECE;
        }

        return IMPOSSIBLE;
    }

    private boolean isForward(Position from, Position to) {
        return getColor().isForward(from, to);
    }

    private boolean isVerticalWay(Position from, Position to) {
        return from.isVerticalWay(to);
    }

    private boolean isValidDistance(Position from, Position to) {
        if (isVerticalWay(from, to)) {
            return getVerticalDistance(from, to) <= movableDistance(from);
        }
        return getVerticalDistance(from, to) == MOVE_DISTANCE && getHorizontalDistance(from, to) == MOVE_DISTANCE;
    }

    private int getVerticalDistance(Position from, Position to) {
        return from.getVerticalDistance(to);
    }

    private int movableDistance(Position from) {
        if (isStartPawnPosition(from)) {
            return START_MOVE_DISTANCE;
        }
        return MOVE_DISTANCE;
    }

    private boolean isStartPawnPosition(Position position) {
        if (getColor() == Color.BLACK) {
            return position.isSameRank(BLACK_START_RANK);
        }
        return position.isSameRank(WHITE_START_RANK);
    }

    private int getHorizontalDistance(Position from, Position to) {
        return from.getHorizontalDistance(to);
    }

    private boolean isDiagonalWay(Position from, Position to) {
        return from.isDiagonalWay(to);
    }

    @Override
    public BigDecimal getPoint() {
        return BigDecimal.ONE;
    }
}
