package wooteco.chess.domain.piece.pathStrategy;

import static wooteco.chess.util.NullValidator.*;

import wooteco.chess.domain.board.Position;
import wooteco.chess.exception.NotMovableException;

public class KnightPathStrategy extends ShortRangePieceStrategy {
	private static final int MOVABLE_DISTANCE_SUM = 3;

	@Override
	public void validateDistance(Position sourcePosition, Position targetPosition) {
		validateNull(sourcePosition, targetPosition);

		int gapSum = sourcePosition.getXYGapSum(targetPosition);
		if (gapSum != MOVABLE_DISTANCE_SUM) {
			throw new NotMovableException(String.format("지정한 위치 %s는 나이트가 이동할 수 없는 곳입니다.", targetPosition.getName()));
		}
	}
}
