package wooteco.chess.domain.piece.strategy;

import java.util.List;
import java.util.Map;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Direction;
import wooteco.chess.domain.piece.Path;
import wooteco.chess.domain.piece.Piece;

public class BasicOneMoveStrategy implements MoveStrategy {
    List<Direction> directions;

    public BasicOneMoveStrategy(List<Direction> directions) {
        this.directions = directions;
    }

    @Override
    public Path findMovablePositions(Path path, Map<Position, Piece> pieces) {
        path.findPathOneTimeByDirections(directions, pieces);
        return path;
    }
}
