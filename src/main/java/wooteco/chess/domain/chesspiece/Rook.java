package wooteco.chess.domain.chesspiece;

import static wooteco.chess.domain.Direction.*;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.Direction;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

public class Rook extends RouteCheckPiece {
	private static final List<Direction> DIRECTIONS;
	private static final String NAME = "r";

	static {
		DIRECTIONS = Arrays.asList(UP, DOWN, LEFT, RIGHT);
	}

	public Rook(Position position, Team team) {
		super(position, team);
	}

	@Override
	public String getName() {
		return team.parseName(NAME);
	}

	@Override
	public boolean isNotNeedCheckPath() {
		return false;
	}

	@Override
	public Positions makePathAndValidate(Piece targetPiece) {
		return moveManager.makePath(targetPiece.position, DIRECTIONS);
	}

}