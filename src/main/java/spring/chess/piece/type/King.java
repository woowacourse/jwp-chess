package spring.chess.piece.type;

import spring.chess.piece.type.movable.KingPieceMovable;
import spring.chess.score.Score;
import spring.chess.team.Team;

public class King extends Piece {
	private static final char NAME = 'k';
	private static final Score SCORE = new Score(0);

	public King(Team team) {
		super(NAME, SCORE, team, new KingPieceMovable());
	}
}
