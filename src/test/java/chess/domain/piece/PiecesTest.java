package chess.domain.piece;

import static chess.domain.OutputFactory.printCurrentBoard;

import chess.domain.board.Board;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

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
}