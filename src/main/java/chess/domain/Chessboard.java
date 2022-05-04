package chess.domain;

import chess.piece.*;

import java.util.*;

public class Chessboard {

    private static final int NUMBER_OF_KINGS_FOR_OVER = 1;

    private final Map<Position, Piece> board;

    public Chessboard(){
        board = new HashMap<>();
    }

    public Chessboard(Map<Position, Piece> board) {
        this.board = board;
    }

    public void move(MovingPosition movingPosition, Color turn) {
        validate(movingPosition, turn);
        movePiece(movingPosition);
    }

    public boolean isOver() {
        int count = (int) board.values()
                .stream()
                .filter(piece -> piece.isSameType(Type.KING))
                .count();

        return count == NUMBER_OF_KINGS_FOR_OVER;
    }

    public Map<Position, Piece> getBoard() {
        return Collections.unmodifiableMap(board);
    }

    private void validate(MovingPosition movingPosition, Color turn) {
        Piece from = board.get(movingPosition.getFrom());
        Piece to = board.get(movingPosition.getTo());

        validateBlank(from);
        validateTurn(from, turn);
        validateColor(from, to);
        validateMovable(movingPosition, from, to);
        validateMiddlePosition(movingPosition, from);
    }

    private void validateBlank(Piece piece) {
        if (piece.isSameType(Type.BLANK)) {
            throw new IllegalArgumentException("이동하려는 위치에 기물이 없습니다.");
        }
    }

    private void validateTurn(Piece from, Color turn) {
        if (!from.isSameColor(turn)) {
            throw new IllegalArgumentException("상대편의 기물은 움직일 수 없습니다.");
        }
    }

    private void validateColor(Piece from, Piece to) {
        if (from.isSameColor(to.getColor())) {
            throw new IllegalArgumentException("상대편의 기물으로만 이동할 수 있습니다.");
        }
    }

    private void validateMovable(MovingPosition movingPosition, Piece from, Piece to) {
        if (!to.isSameType(Type.BLANK) && isDiagonalPawn(movingPosition, from)) {
            return;
        }
        if (from.isMovable(movingPosition)) {
            return;
        }
        throw new IllegalArgumentException("움직일 수 없는 기물입니다.");
    }

    private boolean isDiagonalPawn(MovingPosition movingPosition, Piece from) {
        if (!from.isSameType(Type.PAWN)) {
            return false;
        }
        Pawn pawn = (Pawn) from;
        return pawn.isDiagonal(movingPosition);
    }

    private void validateMiddlePosition(MovingPosition movingPosition, Piece from) {
        from.computeMiddlePosition(movingPosition)
                .forEach(this::validateMiddleBlank);
    }

    private void validateMiddleBlank(Position position) {
        if (!board.get(position).isSameType(Type.BLANK)) {
            throw new IllegalArgumentException("가로막는 기물이 있습니다.");
        }
    }

    private void movePiece(MovingPosition movingPosition) {
        Position from = movingPosition.getFrom();
        Position to = movingPosition.getTo();

        board.put(to, board.get(from));
        board.put(from, new Blank());
    }

}
