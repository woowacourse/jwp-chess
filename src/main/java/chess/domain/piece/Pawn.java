package chess.domain.piece;

import chess.domain.board.ChessBoard;
import chess.domain.board.Direction;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;

import java.util.List;

public class Pawn extends Piece {
    private static final char BLACK_PAWN_START_ROW = '7';
    private static final char WHITE_PAWN_START_ROW = '2';
    private static final int ROW_INDEX = 1;

    public Pawn(Color color, Position position) {
        super(color, position);
        if (color.equals(Color.NO_COLOR)) {
            throw new MovablePieceColorException();
        }
        this.type = Type.PAWN;
    }

    @Override
    public List<Direction> directions() {
        if (isBlack()) {
            return Direction.blackPawnDirection();
        }
        return Direction.whitePawnDirection();
    }

    @Override
    public boolean isMovable(ChessBoard chessBoard, Direction direction, Position targetPosition) {
        if (direction.isLinear()) {
            return isLinearMovable(chessBoard, direction, targetPosition);
        }
        return isDiagonalMovable(chessBoard, direction, targetPosition);
    }

    private boolean isLinearMovable(ChessBoard chessBoard, Direction direction, Position targetPosition) {
        Position nextPosition = this.nextPosition(direction);
        Piece nextPiece = chessBoard.getPiece(nextPosition);
        if (nextPosition.equals(targetPosition) && nextPiece.isBlank()) {
            return true;
        }
        Position twoStepNextPosition = nextPosition.nextPosition(direction);
        Piece twoStepNextPiece = chessBoard.getPiece(twoStepNextPosition);
        return isStartPosition() && twoStepNextPosition.equals(targetPosition) && twoStepNextPiece.isBlank() && nextPiece.isBlank();
    }

    private boolean isStartPosition() {
        if (isBlack()) {
            return getPosition().charAt(ROW_INDEX) == BLACK_PAWN_START_ROW;
        }
        return getPosition().charAt(ROW_INDEX) == WHITE_PAWN_START_ROW;
    }

    private boolean isDiagonalMovable(ChessBoard chessBoard, Direction direction, Position targetPosition) {
        Position nextPosition = this.nextPosition(direction);
        Piece nextPiece = chessBoard.getPiece(nextPosition);
        return nextPosition.equals(targetPosition) && !nextPiece.isBlank() && this.isNotAlly(nextPiece);
    }
}
