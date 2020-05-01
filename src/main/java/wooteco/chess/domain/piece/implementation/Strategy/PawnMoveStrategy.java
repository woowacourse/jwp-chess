package wooteco.chess.domain.piece.implementation.Strategy;

import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.direction.MovingDirection;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Rank;

class PawnMoveStrategy {

    private MovingDirection moveDirection;
    private Rank startRank;

    PawnMoveStrategy(MovingDirection moveDirection, Rank startRank) {
        this.moveDirection = moveDirection;
        this.startRank = startRank;
    }

    List<Position> getMovablePositions(Position source, BoardSituation boardSituation) {
        List<Position> positions = new ArrayList<>();
        if (source.canMoveBy(moveDirection)) {
            Position startPosition = source.moveByDirection(moveDirection);
            if (boardSituation.canMove(startPosition)) {
                positions.add(startPosition);
                addIfStartPosition(source, boardSituation, positions);
            }
        }
        return positions;
    }

    private void addIfStartPosition(Position source, BoardSituation boardSituation, List<Position> positions) {
        if (isFirstMove(source)) {
            Position position = source.moveByDirection(moveDirection).moveByDirection(moveDirection);
            if (boardSituation.canMove(position)) {
                positions.add(position);
            }
        }
    }

    private boolean isFirstMove(Position source) {
        return source.isSameRank(startRank);
    }
}
