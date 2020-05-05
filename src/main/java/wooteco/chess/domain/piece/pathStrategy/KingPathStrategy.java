package wooteco.chess.domain.piece.pathStrategy;

import static wooteco.chess.util.NullValidator.*;

import wooteco.chess.domain.board.Position;
import wooteco.chess.exception.NotMovableException;

public class KingPathStrategy extends ShortRangePieceStrategy {
	private static final int MAX_DISTANCE = 1;

	@Override
	public void validateDistance(Position sourcePosition, Position targetPosition) {
		validateNull(sourcePosition, targetPosition);

		if (sourcePosition.isBiggerGapWith(targetPosition, MAX_DISTANCE)) {
			throw new NotMovableException(String.format("지정한 위치 %s는 킹이 이동할 수 없는 곳입니다.", targetPosition.getName()));
		}
	}
}
