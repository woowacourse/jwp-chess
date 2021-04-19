package chess.domain.piece.condition;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;

import java.util.List;

public class FirstTurnWhitePawnMoveCondition extends MoveCondition {

    @Override
    public boolean isSatisfyBy(final Board board, final Piece piece, final Position target) {
        return !piece.isSamePosition(target) &&
                isFirstPosition(piece) &&
                isRightMovePath(piece, target) &&
                isNotPieceExistOnPath(board, piece, target) &&
                isNotSameColorOnTarget(board, piece, target) &&
                isNotChessPieceOutOfBoard(board, target);
    }

    private boolean isFirstPosition(Piece piece) {
        return piece.getRow() == 1;
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
                                        piece.getRow() < pieceOnBoard.getRow() && pieceOnBoard.getRow() < target.getRow()
                );
    }

}
