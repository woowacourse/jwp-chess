package chess.util;

import chess.domain.board.piece.Color;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.PieceType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceDisplayUtil {

    private static final List<String> DISPLAY_FORMATS = List.of("♟", "♞", "♝", "♜", "♛", "♚");
    private static final String EMPTY_DISPLAY_FORMAT = "";

    private static final Map<Piece, String> displayMap = initDisplayMap();

    private PieceDisplayUtil() {
    }

    public static String toDisplay(Piece piece) {
        return displayMap.getOrDefault(piece, EMPTY_DISPLAY_FORMAT);
    }

    private static Map<Piece, String> initDisplayMap() {
        Map<Piece, String> displayMap = new HashMap<>();
        for (Color color : Color.values()) {
            putEachPiece(displayMap, color);
        }
        return displayMap;
    }

    private static void putEachPiece(Map<Piece, String> displayMap, Color color) {
        PieceType[] pieceTypes = PieceType.values();
        for (int idx = 0; idx < pieceTypes.length; idx++) {
            Piece piece = Piece.of(color, pieceTypes[idx]);
            displayMap.put(piece, DISPLAY_FORMATS.get(idx));
        }
    }
}
