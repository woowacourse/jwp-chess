package wooteco.chess.domain.piece.pawn;

import java.util.Map;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class Pawn extends Piece {
	private static final int PAWN_SCORE = 1;

	private final String symbol = "p";

	public Pawn(Team team, Position position) {
		super(new PawnStrategy(team), team, position);
	}

	// ??
	public static Pawn of(Team team, Position position) {
		return new Pawn(team, position);
	}

	@Override
	public boolean isPawn() {
		return true;
	}

	@Override
	public Piece move(Position from, Position to, Map<Position, Team> teamBoard) {
		strategy.validateMove(from, to, teamBoard);
		this.position = to;
		return this;
	}

	@Override
	public double getScore() {
		return PAWN_SCORE;
	}

	public String getSymbol() {
		return symbol;
	}
}