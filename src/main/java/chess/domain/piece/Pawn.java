package chess.domain.piece;


import chess.domain.board.Score;
import chess.domain.position.Direction;
import chess.domain.position.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pawn extends Piece {

    private static final String NAME = "p";
    private static final String BLACK_INITIAL_ROW = "7";
    private static final String WHITE_INITIAL_ROW = "2";
    private static final Score SCORE = new Score(1);

    public Pawn(Color color) {
        super(NAME, color, SCORE);
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

    private boolean isForwardTwoStepMovable(Map<Position, Piece> board, Direction direction,
        Position source, Position target) {
        if (!isInitialPosition(source) || !board.get(target).isEmpty()) {
            return false;
        }
        Position firstStep = source.sum(direction);
        if (!board.get(firstStep).isEmpty()) {
            return false;
        }
        Position secondStep = firstStep.sum(direction);
        return secondStep.equals(target);
    }

    private boolean isInitialPosition(Position source) {
        if (isBlack()) {
            return source.chessRowCoordinate().equals(BLACK_INITIAL_ROW);
        }
        return source.chessRowCoordinate().equals(WHITE_INITIAL_ROW);
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public boolean canMove(Map<Position, Piece> board, Position source, Position target) {
        List<Direction> directions = new ArrayList<>(findDirections());
        Direction forwardDirection = directions.remove(0);

        return isForwardOneStepMovable(board, forwardDirection, source, target)
            || isForwardTwoStepMovable(board, forwardDirection, source, target)
            || isDiagonalMovable(board, directions, source, target);
    }
}
