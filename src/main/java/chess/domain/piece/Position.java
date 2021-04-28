package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.piece.condition.MoveCondition;
import chess.exception.ChessPieceMoveNotAllowException;

import java.util.List;

public class Position {
    private static final String POSITION_FORMAT = "[a-h][1-8]";

    private final int row;
    private final int column;

    public Position(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    public static Position ofChessPiece(String input) {
        validateRightInput(input);

        return getPositionFromInput(input);
    }

    private static void validateRightInput(String input) {
        if (isRightPositionFormat(input)) {
            return;
        }

        throw new ChessPieceMoveNotAllowException();
    }

    private static boolean isRightPositionFormat(String inputs) {
        return inputs.matches(POSITION_FORMAT);
    }

    private static Position getPositionFromInput(String input) {
        String[] inputs = input.split("");

        int column = inputs[0].charAt(0) - 'a';
        int row = Board.getRow() - Integer.parseInt(inputs[1]);

        return new Position(row, column);
    }

    public double calculateGradient(Position position) {
        return (position.row - row) / (double) (position.column - column);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Position changePosition(Position target, Board board, ChessPiece piece, List<MoveCondition> moveConditions) {
        moveConditions.stream()
                .filter(moveCondition -> moveCondition.isSatisfiedBy(board, piece, target))
                .findAny()
                .orElseThrow(ChessPieceMoveNotAllowException::new);

        return target;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        final Position position = (Position) o;

        if (row != position.row) return false;
        return column == position.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

}
