package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessMap {

    private final Map<String, Character> chessMap;

    private ChessMap(Map<String, Character> chessMap) {
        this.chessMap = chessMap;
    }

    public static ChessMap of(final List<Piece> whitePieces, final List<Piece> blackPieces) {
        final Map<String, Character> chessMap = new HashMap<>();

        chessMap.putAll(markWhitePieces(whitePieces));
        chessMap.putAll(markBlackPieces(blackPieces));

        return new ChessMap(chessMap);
    }

    private static Map<String, Character> markWhitePieces(final List<Piece> whitePieces) {
        Map<String, Character> pieces = new HashMap<>();
        for (Piece piece : whitePieces) {
            final Position position = piece.getPosition();
            final String rank = position.getRank().convertedValue();
            final String file = position.getFile().convertedValue();
            final String positionName = file + rank;
            pieces.put(positionName, Character.toLowerCase(piece.getName()));
        }
        return pieces;
    }

    private static Map<String, Character> markBlackPieces(final List<Piece> blackPieces) {
        Map<String, Character> pieces = new HashMap<>();
        for (Piece piece : blackPieces) {
            final Position position = piece.getPosition();
            final String rank = position.getRank().convertedValue();
            final String file = position.getFile().convertedValue();
            final String positionName = file + rank;
            pieces.put(positionName, piece.getName());
        }
        return pieces;
    }

    public Map<String, Character> getChessMap() {
        return chessMap;
    }
}
