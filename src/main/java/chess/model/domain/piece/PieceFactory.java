package chess.model.domain.piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PieceFactory {

    private static final Map<String, Piece> CACHE;

    static {
        Map<String, Piece> cache = new HashMap<>();
        cache.put("BLACK_PAWN", Pawn.getInstance(Team.BLACK));
        cache.put("WHITE_PAWN", Pawn.getInstance(Team.WHITE));
        cache.put("BLACK_ROOK", Rook.getInstance(Team.BLACK));
        cache.put("WHITE_ROOK", Rook.getInstance(Team.WHITE));
        cache.put("BLACK_KNIGHT", Knight.getInstance(Team.BLACK));
        cache.put("WHITE_KNIGHT", Knight.getInstance(Team.WHITE));
        cache.put("BLACK_BISHOP", Bishop.getInstance(Team.BLACK));
        cache.put("WHITE_BISHOP", Bishop.getInstance(Team.WHITE));
        cache.put("BLACK_QUEEN", Queen.getInstance(Team.BLACK));
        cache.put("WHITE_QUEEN", Queen.getInstance(Team.WHITE));
        cache.put("BLACK_KING", King.getInstance(Team.BLACK));
        cache.put("WHITE_KING", King.getInstance(Team.WHITE));

        CACHE = Collections.unmodifiableMap(cache);
    }

    public static Piece of(String pieceName) {
        return CACHE.keySet().stream()
            .filter(name -> name.equalsIgnoreCase(pieceName))
            .map(CACHE::get)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static Piece of(Team team, Type type) {
        return CACHE.values().stream()
            .filter(piece -> piece.isSameTeam(team))
            .filter(piece -> piece.isSameType(type))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static String getName(Piece piece) {
        return CACHE.keySet().stream()
            .filter(key -> CACHE.get(key) == piece)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
