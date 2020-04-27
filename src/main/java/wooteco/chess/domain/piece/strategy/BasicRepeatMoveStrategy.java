package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Direction;
import wooteco.chess.domain.piece.Path;
import wooteco.chess.domain.piece.Piece;

import java.util.List;
import java.util.Map;

public class BasicRepeatMoveStrategy implements MoveStrategy {
    List<Direction> directions;

    public BasicRepeatMoveStrategy(List<Direction> directions) {
        this.directions = directions;
    }

    @Override
    public Path findMovablePositions(Path path, Map<Position, Piece> pieces) {
        path.findPathManyTimesByDirections(directions, pieces);
        return path;
    }
}
