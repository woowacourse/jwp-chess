package wooteco.chess.domain.piece.implementation.Strategy;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.direction.MovingDirection;
import wooteco.chess.domain.piece.implementation.MoveByDirectionStrategy;
import wooteco.chess.domain.player.Team;

public class BishopStrategy extends MoveByDirectionStrategy {

    private static final List<MovingDirection> DIRECTIONS;

    static {
        DIRECTIONS = Arrays.asList(
            MovingDirection.NORTH_EAST,
            MovingDirection.NORTH_WEST,
            MovingDirection.SOUTH_EAST,
            MovingDirection.SOUTH_WEST
        );
    }

    public BishopStrategy(Team team) {
        super(DIRECTIONS, team);
    }
}
