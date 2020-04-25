package chess.model.domain.piece;

import java.util.HashMap;
import java.util.Map;
import util.NullChecker;

public class Rook extends RepeatMovePiece {

    private final static Map<Team, Piece> CACHE = new HashMap<>();

    static {
        CACHE.put(Team.BLACK, new Rook(Team.BLACK, Type.ROOK));
        CACHE.put(Team.WHITE, new Rook(Team.WHITE, Type.ROOK));
    }

    public Rook(Team team, Type type) {
        super(team, type);
    }

    public static Piece getPieceInstance(Team team) {
        NullChecker.validateNotNull(team);
        return CACHE.get(team);
    }
}

