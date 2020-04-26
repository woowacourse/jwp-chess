package wooteco.chess.domain.chessPiece;

import wooteco.chess.domain.position.Position;

public interface Catchable {

	boolean canCatch(final Position sourcePosition, final Position targetPosition);

}
