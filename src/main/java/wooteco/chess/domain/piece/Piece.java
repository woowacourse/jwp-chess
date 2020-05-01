package wooteco.chess.domain.piece;

import java.util.List;

import wooteco.chess.domain.position.Position;

public abstract class Piece {
	protected Position position;
	protected final Symbol symbol;
	protected final Turn turn;

	public Piece(Position position, Symbol symbol, Turn turn) {
		this.position = position;
		this.symbol = symbol;
		this.turn = turn;
	}

	public abstract boolean canNotMoveTo(Piece that);

	protected abstract List<Position> createMovableArea();

	public abstract boolean isObstacle();

	public abstract boolean hasToAlive();

	public abstract boolean isPenaltyApplier();

	public boolean isSameTeam(Turn turn) {
		return this.turn == turn;
	}

	public boolean isNotSameTeam(Turn turn) {
		return this.turn != turn;
	}

	public Position getPosition() {
		return position;
	}

	public Turn getTurn() {
		return turn;
	}

	public String getSymbol() {
		if (Turn.WHITE == turn) {
			return symbol.getWhiteSymbol();
		}
		return symbol.getBlackSymbol();
	}

	public boolean isPositionEquals(Position position) {
		return this.position.equals(position);
	}
}
