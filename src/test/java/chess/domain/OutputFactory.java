package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Map;

public class OutputFactory {
    public static void printCurrentBoard(final Map<Position, Piece> chessBoard) {
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

    private static int updateLastVerticalValue(final int before, final Position position) {
        int newValue = before;
        if (position.rank().value() != before) {
            newValue = position.rank().value();
            System.out.println(" | " + (newValue + 1));
        }
        return newValue;
    }
}
