package util;

import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.HashMap;
import java.util.Map;

public class PieceConverter {
    private static final Map<Piece, String> converterToName = new HashMap<>();
    private static final Map<String, Piece> converterToPiece = new HashMap<>();

    static {
        converterToName.put(new Pawn(1), "p");
        converterToName.put(new Pawn(-1), "P");
        converterToName.put(new Rook(), "R");
        converterToName.put(new Knight(), "N");
        converterToName.put(new Bishop(), "B");
        converterToName.put(new Queen(), "Q");
        converterToName.put(new King(), "K");

        converterToPiece.put("p", new Pawn(1));
        converterToPiece.put("P", new Pawn(-1));
        converterToPiece.put("R", new Rook());
        converterToPiece.put("N", new Knight());
        converterToPiece.put("B", new Bishop());
        converterToPiece.put("Q", new Queen());
        converterToPiece.put("K", new King());
    }

    private PieceConverter() {
    }

    public static Piece convertToPiece(final String piece) {
        if ("p".equals(piece)) {
            return converterToPiece.get(piece);
        }
        return converterToPiece.get(piece.toUpperCase());
    }

    public static String convertToPieceName(final String color, final Piece piece) {
        String symbol = converterToName.get(piece);
        if ("White".equals(color)) {
            return symbol.toLowerCase();
        }
        return symbol;
    }
}
