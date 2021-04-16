package chess.domain.piece.condition;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;

import java.util.function.Predicate;

public class FirstTurnBlackPawnMoveCondition extends MoveCondition {
    private static final int FIRST_POSITION = 1;

    @Override
    public boolean isSatisfyBy(final Board board, final Piece piece, final Position target) {
        return !piece.isSamePosition(target) &&
                isRightPosition(piece) &&
                isRightMovePath(piece, target) &&
                isNotExistPieceOnPath(board, piece, target) &&
                isNotChessPieceOutOfBoard(board, target);
    }

    private boolean isRightPosition(Piece piece) {
        return piece.getRow() == FIRST_POSITION;
    }

    private boolean isRightMovePath(final Piece piece, final Position target) {
        return target.equals(new Position(piece.getRow() + 2, piece.getColumn()));
    }

    private boolean isNotExistPieceOnPath(Board board, Piece piece, Position target) {
        return board.isNoneMatchByFilteredPieces(pieceOnBoard -> !pieceOnBoard.equals(piece),
                isExistInMoveArea(piece, target));
    }

    private Predicate<Piece> isExistInMoveArea(final Piece piece, final Position target) {
        return pieceOnBoard -> pieceOnBoard.getColumn() == piece.getColumn() &&
                piece.getRow() <= pieceOnBoard.getRow() && pieceOnBoard.getRow() <= target.getRow();
    }

}
