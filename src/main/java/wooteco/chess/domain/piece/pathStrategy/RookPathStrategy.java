package wooteco.chess.domain.piece.pathStrategy;

import static wooteco.chess.util.NullValidator.*;

import wooteco.chess.domain.board.Position;
import wooteco.chess.exception.NotMovableException;

public class RookPathStrategy extends LongRangePieceStrategy {
	@Override
	public void validateDistance(Position sourcePosition, Position targetPosition) {
		validateNull(sourcePosition, targetPosition);

		if (sourcePosition.isDifferentXPoint(targetPosition) && sourcePosition.isDifferentYPoint(targetPosition)) {
			throw new NotMovableException(String.format("지정한 위치 %s는 룩이 이동할 수 없는 곳입니다.", targetPosition.getName()));
		}
	}
}
