package wooteco.chess.domain.piece.implementation;

import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.direction.MovingDirection;
import wooteco.chess.domain.piece.MoveStrategy;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public abstract class MoveByDirectionStrategy implements MoveStrategy {

    private List<MovingDirection> directions;
    private Team team;

    public MoveByDirectionStrategy(List<MovingDirection> directions, Team team) {
        this.directions = directions;
        this.team = team;
    }

    @Override
    public List<Position> getMovablePositions(Position source, BoardSituation boardSituation) {
        List<Position> positions = new ArrayList<>();
        for (MovingDirection direction : directions) {
            positions.addAll(getMovablePositionsByDirection(direction, source, boardSituation));
        }
        return positions;
    }

    private List<Position> getMovablePositionsByDirection(MovingDirection direction, Position source,
        BoardSituation boardSituation) {
        List<Position> positions = new ArrayList<>();
        Position startPosition = source;
        while (startPosition.canMoveBy(direction)) {
            startPosition = startPosition.moveByDirection(direction);
            if (boardSituation.canMoveOrAttack(startPosition, team)) {
                positions.add(startPosition);
            }
            if (!boardSituation.canMove(startPosition)) {
                break;
            }
        }
        return positions;
    }
}
