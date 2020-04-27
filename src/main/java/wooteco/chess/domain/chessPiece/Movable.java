package wooteco.chess.domain.chessPiece;

import wooteco.chess.domain.position.Position;

public interface Movable {

	boolean canLeap();

	boolean canMove(final Position sourcePosition, final Position targetPosition);

}
