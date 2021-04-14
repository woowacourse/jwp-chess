package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

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

    @Test
    @DisplayName("빈 board 생성 확인 ")
    void emptyBoard() {
        Board board = Board.emptyBoard();

        Map<Position, Piece> chessBoard = board.chessBoard();

        printCurrentBoard(chessBoard);

        assertThat(chessBoard).hasSize(64);
    }

    @Test
    @DisplayName("board에 pieces를 넣는다. ")
    void assignPieces() {
        Board board = Board.emptyBoard();
        Pieces pieces = new Pieces(Arrays.asList(new King(Color.BLACK, new Position("a", "2"))));

        Board assignedBoard = board.put(pieces);

        printCurrentBoard(assignedBoard.chessBoard());
    }

}