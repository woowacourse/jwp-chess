package wooteco.chess.domain.state;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.piece.blank.Blank;
import wooteco.chess.domain.piece.pawn.Pawn;
import wooteco.chess.domain.position.Position;

public class BoardFactory {
    private static final int BLACK_PAWN = 2;
    private static final int WHITE_PAWN = 7;
    private static final int BLANK_START_COLUMN = 3;
    private static final int BLANK_END_COLUMN = 6;
    private static final int FIRST_ROW = 1;
    private static final int LAST_ROW = 8;

    public static Board create() {
        TreeMap<Position, Piece> initialBoard = new TreeMap<>();
        loadBlank(initialBoard);
        loadPawn(initialBoard);
        loadOthers(initialBoard);
        return new Board(initialBoard);
    }

    private static void loadBlank(TreeMap<Position, Piece> initialBoard) {
        for (int i = BLANK_START_COLUMN; i <= BLANK_END_COLUMN; i++) {
            for (int j = FIRST_ROW; j <= LAST_ROW; j++) {
                initialBoard.put(Position.of(j, i), new Blank(Position.of(j, i)));
            }
        }
    }

    private static void loadPawn(TreeMap<Position, Piece> initialBoard) {
        for (int i = FIRST_ROW; i <= LAST_ROW; i++) {
            initialBoard.put(Position.of(i, BLACK_PAWN),
                Pawn.of(Team.WHITE, Position.of(i, BLACK_PAWN)));
            initialBoard.put(Position.of(i, WHITE_PAWN),
                Pawn.of(Team.BLACK, Position.of(i, WHITE_PAWN)));
        }
    }

    private static void loadOthers(TreeMap<Position, Piece> initialBoard) {
        List<String> pieceNames = Arrays.asList("r", "n", "b", "q", "k", "b", "n", "r");
        for (int i = 1; i <= pieceNames.size(); i++) {
            initialBoard.put(
                Position.of(i, 1),
                PieceFactory.of(pieceNames.get(i - 1), "white", i, 1)
            );
            initialBoard.put(
                Position.of(i, 8),
                PieceFactory.of(pieceNames.get(i - 1), "black", i, 8)
            );
        }
    }
}
