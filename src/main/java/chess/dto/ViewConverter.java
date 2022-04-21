package chess.dto;

import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.pieces.Symbol;
import chess.domain.position.Column;

import java.util.Map;

public class ViewConverter {

    private static final int DIAGONAL_NUMBER = 2;

    public static String findBackground(Integer row, Column column) {
        if ((column.value() + row) % DIAGONAL_NUMBER == 0) {
            return "black";
        }
        return "white";
    }

    public static String findImageName(Map<String, Piece> pieces, Integer row, Column column) {
        if (pieces.containsKey(row + column.name())) {
            final Piece piece = pieces.get(row + column.name());
            if (piece.isSameColor(Color.WHITE)) {
                return "white-" + piece.symbol() + ".png";
            }
            return "black-" + piece.symbol() + ".png";
        }
        return Symbol.BLANK.value();
    }
}
