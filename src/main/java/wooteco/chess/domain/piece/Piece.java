package wooteco.chess.domain.piece;

import java.util.List;

import wooteco.chess.domain.position.Position;

public abstract class Piece {
	protected Position position;
	protected final Symbol symbol;
	protected final Team team;

	public Piece(Position position, Symbol symbol, Team team) {
		this.position = position;
		this.symbol = symbol;
		this.team = team;
	}

	public abstract boolean canNotMoveTo(Piece that);

	protected abstract List<Position> createMovableArea();

	public abstract boolean isObstacle();

	public abstract boolean hasToAlive();

	public abstract boolean isPenaltyApplier();

	public boolean isSameTeam(Team team) {
		return this.team == team;
	}

	public boolean isNotSameTeam(Team team) {
		return this.team != team;
	}

	public Position getPosition() {
		return position;
	}

	public Team getTeam() {
		return team;
	}

	public String getSymbol() {
		if (Team.WHITE == team) {
			return symbol.getWhiteSymbol();
		}
		return symbol.getBlackSymbol();
	}

	public boolean isPositionEquals(Position position) {
		return this.position.equals(position);
	}
}
