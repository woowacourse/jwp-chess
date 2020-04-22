package chess.model.domain.piece;

import java.util.HashMap;
import java.util.Map;
import util.NullChecker;

public class Queen extends RepeatMovePiece {

    private final static Map<Team, Piece> CACHE = new HashMap<>();

    static {
        CACHE.put(Team.BLACK, new Queen(Team.BLACK, Type.QUEEN));
        CACHE.put(Team.WHITE, new Queen(Team.WHITE, Type.QUEEN));
    }

    public Queen(Team team, Type type) {
        super(team, type);
    }

    public static Piece getPieceInstance(Team team) {
        NullChecker.validateNotNull(team);
        return CACHE.get(team);
    }
}
