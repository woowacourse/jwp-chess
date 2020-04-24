package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.position.Position;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {
    private static final Map<Position, Piece> BOARD = new HashMap<>();
    private static final int ASCII_GAP = 96;

    static {
        setUpBlank();
    }

    private static void setUpBlank() {
        for (int row = Position.START_INDEX; row <= Position.END_INDEX; row++) {
            for (int col = Position.START_INDEX; col <= Position.END_INDEX; col++) {
                String position = (char) (col + ASCII_GAP) + String.valueOf(row);
                BOARD.put(Position.of(position), Piece.of(PieceType.BLANK));
            }
        }
    }

    private BoardFactory() {
    }

    public static Board createBoard() {
        return new Board(BOARD);
    }
}
