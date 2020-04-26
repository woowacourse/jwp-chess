package wooteco.chess.domain.piece.implementation.Strategy;

import wooteco.chess.domain.direction.MovingDirection;
import wooteco.chess.domain.piece.implementation.MoveByDistanceStrategy;
import wooteco.chess.domain.player.Team;

import java.util.Arrays;
import java.util.List;

public class KnightStrategy extends MoveByDistanceStrategy {

    private static final List<MovingDirection> DIRECTIONS;

    static {
        DIRECTIONS = Arrays.asList(
                MovingDirection.NORTH_NORTH_EAST,
                MovingDirection.NORTH_EAST_EAST,
                MovingDirection.SOUTH_EAST_EAST,
                MovingDirection.SOUTH_SOUTH_EAST,
                MovingDirection.SOUTH_SOUTH_WEST,
                MovingDirection.SOUTH_WEST_WEST,
                MovingDirection.NORTH_WEST_WEST,
                MovingDirection.NORTH_NORTH_WEST
        );
    }

    public KnightStrategy(Team team) {
        super(DIRECTIONS, team);
    }
}
