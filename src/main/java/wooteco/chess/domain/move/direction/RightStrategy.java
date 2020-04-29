package wooteco.chess.domain.move.direction;

import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

import java.util.ArrayList;
import java.util.List;

public class RightStrategy implements DirectionStrategy {
    @Override
    public List<Position> findPath(Position source, Position target) {
        List<Position> path = new ArrayList<>();

        for (int i = target.getFile() - 1; i > source.getFile(); i--) {
            path.add(Positions.of(i, source.getRank()));
        }
        return path;
    }
}