package wooteco.chess.domain.piece.blank;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.piece.MovingStrategy;
import wooteco.chess.domain.position.Position;

import java.util.Map;

public class BlankStrategy extends MovingStrategy {
	@Override
	protected void checkObstacle(Position source, Position target, Map<Position, Team> teamBoard) {

	}

	@Override
	protected void checkDirection(Position source, Position target) {

	}
}
