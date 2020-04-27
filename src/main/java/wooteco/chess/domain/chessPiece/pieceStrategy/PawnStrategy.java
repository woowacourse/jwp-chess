package wooteco.chess.domain.chessPiece.pieceStrategy;

import wooteco.chess.domain.position.Position;

public abstract class PawnStrategy implements PieceStrategy {

	private static final int PAWN_MOVABLE_FILE_GAP = 0;
	private static final int PAWN_CATCHABLE_FILE_GAP = 1;
	private static final int PAWN_CATCHABLE_RANK_GAP = 1;

	@Override
	public boolean canLeap() {
		return false;
	}

	@Override
	public boolean canMove(final Position sourcePosition, final Position targetPosition, final int pawnMovableRange) {
		return canMoveDirection(sourcePosition, targetPosition) && canMoveRange(sourcePosition, targetPosition,
			pawnMovableRange);
	}

	protected abstract boolean canMoveDirection(Position sourcePosition, Position targetPosition);

	private boolean canMoveRange(final Position sourcePosition, final Position targetPosition,
		final int pawnMovableRange) {
		final int chessFileGap = Math.abs(sourcePosition.calculateChessFileGapTo(targetPosition));
		final int chessRankGap = Math.abs(sourcePosition.calculateChessRankGapTo(targetPosition));

		return (chessFileGap == PAWN_MOVABLE_FILE_GAP) && (chessRankGap <= pawnMovableRange);
	}

	@Override
	public boolean canCatch(final Position sourcePosition, final Position targetPosition) {
		return canCatchDirection(sourcePosition, targetPosition) && canCatchRange(sourcePosition, targetPosition);
	}

	protected abstract boolean canCatchDirection(Position sourcePosition, Position targetPosition);

	private boolean canCatchRange(Position sourcePosition, Position targetPosition) {
		int chessFileGap = Math.abs(sourcePosition.calculateChessFileGapTo(targetPosition));
		int chessRankGap = Math.abs(sourcePosition.calculateChessRankGapTo(targetPosition));

		return (chessFileGap == PAWN_CATCHABLE_FILE_GAP) && (chessRankGap == PAWN_CATCHABLE_RANK_GAP);
	}
}
