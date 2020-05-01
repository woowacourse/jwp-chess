package wooteco.chess.domain.piece.implementation.Strategy;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.direction.MovingDirection;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

class PawnAttackStrategy {

    private List<MovingDirection> attackDirections;
    private Team team;

    PawnAttackStrategy(List<MovingDirection> attackDirections, Team team) {
        this.attackDirections = attackDirections;
        this.team = team;
    }

    List<Position> getMovablePositions(Position source, BoardSituation boardSituation) {
        return attackDirections.stream()
            .filter(source::canMoveBy)
            .map(source::moveByDirection)
            .filter(position -> boardSituation.canAttack(position, team))
            .collect(Collectors.toList());
    }
}
