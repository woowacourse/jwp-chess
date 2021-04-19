package chess.domain.piece.condition;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.function.Predicate;

public class RookMoveCondition extends MoveCondition {

    @Override
    public boolean isSatisfyBy(final Board board, final Piece piece, final Position target) {
        return !piece.isSamePosition(target) &&
            isRightMovable(piece, target) &&
            isNotExistObstacleOnCrossPath(board, piece, target) &&
            isNotSameColorOnTarget(board, piece, target) &&
            isNotChessPieceOutOfBoard(board, target);
    }

    private boolean isRightMovable(final Piece piece, final Position target) {
        return piece.getRow() == target.getRow() ||
            piece.getColumn() == target.getColumn();
    }

    private boolean isNotExistObstacleOnCrossPath(Board board, Piece piece, Position target) {
        return (isHorizontalMovable(piece, target) &&
            isNotExistObstacleOnHorizontalPath(board, piece, target))

            || (isVerticalMovable(piece, target) &&
            isNotExistObstacleOnVerticalPath(board, piece, target));
    }

    private boolean isHorizontalMovable(final Piece piece, final Position target) {
        return piece.getRow() == target.getRow();
    }

    private boolean isVerticalMovable(final Piece piece, final Position target) {
        return piece.getColumn() == target.getColumn();
    }

    private boolean isNotExistObstacleOnHorizontalPath(final Board board, final Piece piece,
        final Position target) {
        int maxCol = Math.max(piece.getColumn(), target.getColumn());
        int minCol = Math.min(piece.getColumn(), target.getColumn());

        return board.getPieces().stream()
            .filter(pieceOnBoard -> !pieceOnBoard.equals(piece))
            .noneMatch(pieceOnBoard ->
                (pieceOnBoard.getRow() == piece.getRow()) &&
                    (minCol < pieceOnBoard.getColumn()) &&
                    (pieceOnBoard.getColumn() < maxCol)
            );
    }

    private boolean isNotExistObstacleOnVerticalPath(final Board board, final Piece piece,
        final Position target) {
        int maxRow = Math.max(piece.getRow(), target.getRow());
        int minRow = Math.min(piece.getRow(), target.getRow());

        return board.getPieces().stream()
            .filter(pieceOnBoard -> !pieceOnBoard.equals(piece))
            .noneMatch(pieceOnBoard ->
                (pieceOnBoard.getColumn() == piece.getColumn()) &&
                    (minRow < pieceOnBoard.getRow()) &&
                    (pieceOnBoard.getRow() < maxRow)
            );
    }
}
