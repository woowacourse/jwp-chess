package wooteco.chess.domain.chesspiece;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.position.Position;

public class Knight extends RouteUncheckPiece {
	private static final String NAME = "n";

	public Knight(Position position, Team team) {
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
		moveManager.validateKnightMove(targetPiece.position);
	}
}