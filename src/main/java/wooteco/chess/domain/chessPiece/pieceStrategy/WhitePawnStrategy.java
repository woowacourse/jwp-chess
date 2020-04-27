package wooteco.chess.domain.chessPiece.pieceStrategy;

import static wooteco.chess.domain.chessPiece.pieceType.PieceDirection.*;

import wooteco.chess.domain.position.Position;

public class WhitePawnStrategy extends PawnStrategy {

	@Override
	protected boolean canMoveDirection(final Position sourcePosition, final Position targetPosition) {
		return WHITE_PAWN.getMovableDirections().stream()
			.anyMatch(moveDirection -> moveDirection.isSameDirectionFrom(sourcePosition, targetPosition));
	}

	@Override
	protected boolean canCatchDirection(Position sourcePosition, Position targetPosition) {
		return WHITE_PAWN.getCatchableDirections().stream()
			.anyMatch(catchableDirections -> catchableDirections.isSameDirectionFrom(sourcePosition, targetPosition));
	}

}
