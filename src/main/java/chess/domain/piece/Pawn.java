package chess.domain.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.domain.board.Score;
import chess.domain.position.Direction;
import chess.domain.position.Position;

public class Pawn extends Piece {

    private static final String NAME = "p";
    private static final Score SCORE = new Score(1);
    private boolean isMoved;

    public Pawn(Color color) {
        super(NAME, color, SCORE);
        isMoved = false;
    }

    private List<Direction> findDirections() {
        if (isBlack()) {
            return Direction.blackPawnDirection();
        }
        return Direction.whitePawnDirection();
    }

    private boolean isForwardOneStepMovable(Map<Position, Piece> board, Direction direction,
        Position source,
        Position target) {
        return board.get(target).isEmpty() && source.sum(direction).equals(target);
    }

    private boolean isDiagonalMovable(Map<Position, Piece> board, List<Direction> directions,
        Position source,
        Position target) {
        return !board.get(target).isEmpty() && directions.stream()
            .anyMatch(direction -> source.sum(direction).equals(target));
    }

    private boolean isForwardTwoStepMovable(Map<Position, Piece> board, Direction direction, Position source,
        Position target) {
        if (!source.isInitialPawnRow(super.getColor()) || !board.get(target).isEmpty()) {
            return false;
        }

        Position firstStep = source.sum(direction);
        if (!board.get(firstStep).isEmpty()) {
            return false;
        }

        Position secondStep = firstStep.sum(direction);
        return secondStep.equals(target);
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public boolean canMove(Map<Position, Piece> board, Position source, Position target) {
        List<Direction> directions = new ArrayList<>(findDirections());
        Direction forwardDirection = directions.remove(0);

        if (isForwardOneStepMovable(board, forwardDirection, source, target)
            || isForwardTwoStepMovable(board, forwardDirection, source, target)
            || isDiagonalMovable(board, directions, source, target)) {
            isMoved = true;
            return true;
        }
        return false;
    }
}
