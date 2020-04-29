package wooteco.chess.domain.piece;

import wooteco.chess.domain.position.Position;

public interface Movable {
	void move(Position targetPosition);

	boolean canMove(Position targetPosition);
}
