package wooteco.chess.domain.piece.knight;

import java.util.Map;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class Knight extends Piece {
	private static final double KNIGHT_SCORE = 2.5;

	private final String symbol = "n";

	public Knight(Team team, Position position) {
		super(new KnightStrategy(), team, position);
	}

	public static Knight of(Team team, Position position) {
		return new Knight(team, position);
	}

	@Override
	public Piece move(Position from, Position to, Map<Position, Team> dto) {
		strategy.validateMove(from, to, dto);
		this.position = to;
		return this;
	}

	@Override
	public double getScore() {
		return KNIGHT_SCORE;
	}

	public String getSymbol() {
		return symbol;
	}
}