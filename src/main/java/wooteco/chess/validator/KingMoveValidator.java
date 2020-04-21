package wooteco.chess.validator;

import wooteco.chess.Board;
import wooteco.chess.position.Position;

import java.util.Collections;
import java.util.List;

public class KingMoveValidator extends MoveValidator {
    @Override
    protected boolean isNotPermittedMovement(Board board, Position source, Position target) {
        return source.isNotDistanceOneSquare(target);
    }

    @Override
    protected List<Position> movePathExceptSourceAndTarget(Position source, Position target) {
        return Collections.emptyList();
    }
}
