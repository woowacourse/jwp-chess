package wooteco.chess.domain.chessPiece.pieceStrategy;

import wooteco.chess.domain.position.Position;

public class KingStrategy implements PieceStrategy {

	private static final int KING_FILE_GAP = 1;
	private static final int KING_RANK_GAP = 1;

	@Override
	public boolean canLeap() {
		return true;
	}

	@Override
	public boolean canMove(final Position sourcePosition, final Position targetPosition, final int pawnMovableRange) {
		return canMove(sourcePosition, targetPosition);
	}

	@Override
	public boolean canCatch(final Position sourcePosition, final Position targetPosition) {
		return canMove(sourcePosition, targetPosition);
	}

	private boolean canMove(final Position sourcePosition, final Position targetPosition) {
		final int chessFileGap = Math.abs(sourcePosition.calculateChessFileGapTo(targetPosition));
		final int chessRankGap = Math.abs(sourcePosition.calculateChessRankGapTo(targetPosition));

		return chessFileGap <= KING_FILE_GAP && chessRankGap <= KING_RANK_GAP;
	}

}
