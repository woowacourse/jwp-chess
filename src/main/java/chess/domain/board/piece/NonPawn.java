package chess.domain.board.piece;

import chess.domain.board.position.Position;
import chess.exception.InternalServerException;

final class NonPawn extends Piece {

    private static final String INVALID_TYPE_EXCEPTION_MESSAGE = "해당 타입은 폰이 될 수 없습니다.";

    NonPawn(Color color, PieceType type) {
        super(color, type);
        validateNonPawn();
    }

    private void validateNonPawn() {
        if (hasTypeOf(PieceType.PAWN)) {
            throw new InternalServerException(INVALID_TYPE_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public boolean canMove(Position from, Position to) {
        return type.isMovable(from, to);
    }

    @Override
    protected boolean isAttackableRoute(Position from, Position to) {
        return canMove(from, to);
    }
}
