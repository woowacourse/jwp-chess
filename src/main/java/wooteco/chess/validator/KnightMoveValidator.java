package wooteco.chess.validator;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;

import java.util.Collections;
import java.util.List;

public class KnightMoveValidator extends MoveValidator {
    private static final int MULTIPLICATION_OF_BETWEEN_FILE_DISTANCE_AND_RANK_DISTANCE = 2;

    @Override
    protected boolean isNotPermittedMovement(Board board, Position source, Position target) {
        return source.multiplicationOfDifferenceBetweenFileAndRank(target) != MULTIPLICATION_OF_BETWEEN_FILE_DISTANCE_AND_RANK_DISTANCE;
    }

    @Override
    protected List<Position> movePathExceptSourceAndTarget(Position source, Position target) {
        return Collections.emptyList();
    }
}
