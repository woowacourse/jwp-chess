package chess.util;

import chess.domain.board.piece.Color;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.PieceType;
import java.util.HashMap;
import java.util.Map;

public class PieceDisplayUtil {

    private PieceDisplayUtil() {
    }

    public static String toWebDisplay(Piece piece) {
        return PieceDisplayMap.colorlessDisplayOf(piece);
    }

    private static class PieceDisplayMap {

        static final String[] COLORLESS_DISPLAY_FORMATS = "♟♞♝♜♛♚".split("");
        static final String WEB_EMPTY_DISPLAY_FORMAT = "";

        static final Map<Piece, String> colorlessDisplayMap;

        static {
            colorlessDisplayMap = initColorlessDisplayMap();
        }

        static Map<Piece, String> initColorlessDisplayMap() {
            Map<Piece, String> colorlessDisplayMap = new HashMap<>();
            for (Color color : Color.values()) {
                putEachPiece(colorlessDisplayMap, color);
            }
            return colorlessDisplayMap;
        }

        static void putEachPiece(Map<Piece, String> displayMap, Color color) {
            PieceType[] pieceTypes = PieceType.values();
            for (int idx = 0; idx < pieceTypes.length; idx++ ){
                Piece piece = Piece.of(color, pieceTypes[idx]);
                displayMap.put(piece, PieceDisplayMap.COLORLESS_DISPLAY_FORMATS[idx]);
            }
        }

        static String colorlessDisplayOf(Piece piece) {
            return colorlessDisplayMap.getOrDefault(piece, WEB_EMPTY_DISPLAY_FORMAT);
        }
    }
}
