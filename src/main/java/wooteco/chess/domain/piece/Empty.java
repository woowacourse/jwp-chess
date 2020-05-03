package wooteco.chess.domain.piece;

import java.util.List;

import wooteco.chess.domain.position.Position;

public class Empty extends Piece {
	public Empty(Position position) {
		super(position, Symbol.EMPTY, Turn.NONE);
	}

	@Override
	public boolean canNotMoveTo(Piece that) {
		throw new IllegalAccessError();
	}

	@Override
	protected List<Position> createMovableArea() {
		throw new IllegalAccessError();
	}

	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public boolean hasToAlive() {
		return false;
	}

	@Override
	public boolean isPenaltyApplier() {
		return false;
	}
}
