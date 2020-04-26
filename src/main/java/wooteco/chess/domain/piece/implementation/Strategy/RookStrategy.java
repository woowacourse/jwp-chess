package wooteco.chess.domain.piece.implementation.Strategy;

import wooteco.chess.domain.direction.MovingDirection;
import wooteco.chess.domain.piece.implementation.MoveByDirectionStrategy;
import wooteco.chess.domain.player.Team;

import java.util.Arrays;
import java.util.List;

public class RookStrategy extends MoveByDirectionStrategy {

    private static final List<MovingDirection> DIRECTIONS;

    static {
        DIRECTIONS = Arrays.asList(
                MovingDirection.NORTH,
                MovingDirection.EAST,
                MovingDirection.SOUTH,
                MovingDirection.WEST
        );
    }

    public RookStrategy(Team team) {
        super(DIRECTIONS, team);
    }
}
