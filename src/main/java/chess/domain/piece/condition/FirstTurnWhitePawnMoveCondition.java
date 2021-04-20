package chess.domain.piece.condition;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.List;

public class FirstTurnWhitePawnMoveCondition extends MoveCondition {

    private static final int WHITE_PAWN_INITIAL_ROW = 1;

    @Override
    public boolean isSatisfyBy(final Board board, final Piece piece, final Position target) {
        return !piece.isSamePosition(target) &&
            isInitialPosition(piece) &&
            isRightMovePath(piece, target) &&
            isNotPieceExistOnPath(board, piece, target) &&
            isNotSameColorOnTarget(board, piece, target) &&
            isNotChessPieceOutOfBoard(board, target);
    }

    private boolean isInitialPosition(final Piece piece) {
        return piece.getRow() == WHITE_PAWN_INITIAL_ROW;
    }

    private boolean isRightMovePath(final Piece piece, final Position target) {
        return target.equals(new Position(piece.getRow() + 2, piece.getColumn()));
    }

    private boolean isNotPieceExistOnPath(Board board, Piece piece, Position target) {
        List<Piece> pieces = board.getPieces();

        return pieces.stream()
            .filter(pieceOnBoard -> !pieceOnBoard.equals(piece))
            .noneMatch(
                pieceOnBoard ->
                    pieceOnBoard.getColumn() == piece.getColumn() &&
                        piece.getRow() < pieceOnBoard.getRow() && pieceOnBoard.getRow() < target
                        .getRow()
            );
    }

}
