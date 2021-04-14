package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

class PiecesTest {

    @Test
    void move() {
        King king = new King(Color.BLACK, new Position("a2"));
        Pieces pieces = new Pieces(Arrays.asList(king));
        Board board = new Board(pieces);

        printCurrentBoard(board.chessBoard());

        pieces.move(new Source(king), new Target(new Blank(new Position("a3"))));

        Board changedBoard = board.put(pieces);

        printCurrentBoard(changedBoard.chessBoard());


    }

    public void printCurrentBoard(final Map<Position, Piece> chessBoard) {
        System.out.println();
        int lastVerticalValue = 8;
        for (final Position position : chessBoard.keySet()) {
            lastVerticalValue = updateLastVerticalValue(lastVerticalValue, position);
            System.out.print(chessBoard.get(position).name());
        }
        System.out.println(" | " + lastVerticalValue);
        System.out.println("---------");
        System.out.println("abcdefgh");
    }

    private int updateLastVerticalValue(final int before, final Position position) {
        int newValue = before;
        if (position.rank().value() != before) {
            newValue = position.rank().value();
            System.out.println(" | " + (newValue + 1));
        }
        return newValue;
    }

}