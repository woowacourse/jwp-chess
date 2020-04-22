package chess.model.domain.piece;

import java.util.HashMap;
import java.util.Map;
import util.NullChecker;

public class Bishop extends RepeatMovePiece {

    private final static Map<Team, Piece> CACHE = new HashMap<>();

    static {
        CACHE.put(Team.BLACK, new Bishop(Team.BLACK, Type.BISHOP));
        CACHE.put(Team.WHITE, new Bishop(Team.WHITE, Type.BISHOP));
    }

    public Bishop(Team team, Type type) {
        super(team, type);
    }

    public static Piece getPieceInstance(Team team) {
        NullChecker.validateNotNull(team);
        return CACHE.get(team);
    }
}
