package chess.domain.piece.direction;

import chess.domain.position.Position;
import java.util.List;

public interface MoveStrategy {
    Position move(final Position position);

    boolean isSameDirection(final int fileDegree, final int rankDegree);

    boolean isNothing();

    boolean isNorth();

    boolean isNorthWest();

    boolean isNorthEast();

    boolean isInitialPawnNorth();

    int fileDegree();

    int rankDegree();
}
