package wooteco.chess.domain.chesspiece;

import static wooteco.chess.domain.Direction.*;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.Direction;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.position.Position;

public class King extends RouteUncheckPiece {
	private static final List<Direction> DIRECTIONS;
	private static final String NAME = "k";

	static {
		DIRECTIONS = Arrays.asList(UP, LEFT, RIGHT, DOWN, RIGHT_DOWN,
			RIGHT_UP, LEFT_DOWN, LEFT_UP);
	}

	public King(Position position, Team team) {
		super(position, team);
	}

	@Override
	public String getName() {
		return team.parseName(NAME);
	}

	@Override
	public boolean isNotNeedCheckPath() {
		return true;
	}

	@Override
	public void validateCanGo(Piece targetPiece) {
		Direction direction = moveManager.getMatchDirection(targetPiece.position);
		moveManager.validateMove(direction, DIRECTIONS);
	}
}