package chess.domain.piece.condition;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.List;
import java.util.function.Predicate;

public class QueenMoveCondition extends MoveCondition {

    @Override
    public boolean isSatisfyBy(final Board board, final Piece piece, final Position target) {
        return !piece.isSamePosition(target) &&
            isMovablePath(piece, target) &&
            isNotExistObstacleOnPath(board, piece, target) &&
            isNotSameColorOnTarget(board, piece, target) &&
            isNotChessPieceOutOfBoard(board, target);
    }

    private boolean isNotExistObstacleOnPath(Board board, Piece piece, Position target) {
        return
            (isHorizontalMovable(piece, target) &&
                isNotExistObstacleOnHorizontalPath(board, piece, target))

                || (isVerticalMovable(piece, target) &&
                isNotExistObstacleOnVerticalPath(board, piece, target))

                || (isRightXCondition(piece, target) &&
                isNotExistObstacleOnXPath(board, piece, target));
    }

    private boolean isMovablePath(final Piece piece, final Position target) {
        return isRightXCondition(piece, target) || isCrossMovable(piece, target);
    }

    private boolean isCrossMovable(final Piece piece, final Position target) {
        return isHorizontalMovable(piece, target) || isVerticalMovable(piece, target);
    }

    private boolean isHorizontalMovable(final Piece piece, final Position target) {
        return piece.getRow() == target.getRow();
    }

    private boolean isVerticalMovable(final Piece piece, final Position target) {
        return piece.getColumn() == target.getColumn();
    }

    private boolean isRightXCondition(final Piece piece, final Position target) {
        return Math.abs(piece.getColumn() - target.getColumn())
            == Math.abs(piece.getRow() - target.getRow());
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

    private boolean isNotExistObstacleOnXPath(Board board, Piece piece, Position target) {
        List<Piece> pieces = board.getPieces();

        return pieces.stream()
            .filter(isExistInMoveArea(piece, target))
            .noneMatch(hasSameGradientWithSourceAndTarget(piece, target));
    }

    private Predicate<Piece> isExistInMoveArea(Piece piece, Position target) {
        int maxCol = Math.max(piece.getColumn(), target.getColumn());
        int minCol = Math.min(piece.getColumn(), target.getColumn());
        int maxRow = Math.max(piece.getRow(), target.getRow());
        int minRow = Math.min(piece.getRow(), target.getRow());

        return pieceOnBoard -> minCol < pieceOnBoard.getColumn()
            && pieceOnBoard.getColumn() < maxCol &&
            minRow < pieceOnBoard.getRow() && pieceOnBoard.getRow() < maxRow;
    }

    private Predicate<Piece> hasSameGradientWithSourceAndTarget(final Piece piece,
        final Position target) {
        return selectedPiece ->
            piece.getPosition().calculateGradient(target) ==
                piece.getPosition().calculateGradient(selectedPiece.getPosition());
    }

}
