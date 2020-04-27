package wooteco.chess.domain.chessPiece.pieceStrategy;

import static wooteco.chess.domain.chessPiece.pieceType.PieceDirection.*;

import wooteco.chess.domain.position.Position;

public class BlackPawnStrategy extends PawnStrategy {

	@Override
	protected boolean canMoveDirection(final Position sourcePosition, final Position targetPosition) {
		return BLACK_PAWN.getMovableDirections().stream()
			.anyMatch(moveDirection -> moveDirection.isSameDirectionFrom(sourcePosition, targetPosition));
	}

	@Override
	protected boolean canCatchDirection(Position sourcePosition, Position targetPosition) {
		return BLACK_PAWN.getCatchableDirections().stream()
			.anyMatch(catchableDirections -> catchableDirections.isSameDirectionFrom(sourcePosition, targetPosition));
	}

}
