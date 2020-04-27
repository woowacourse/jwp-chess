package chess.model.domain.piece;

import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.Square;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import util.NullChecker;

public class Knight extends OneTimeMovePiece {

    private final static Map<Team, Piece> CACHE = new HashMap<>();

    static {
        CACHE.put(Team.BLACK, new Knight(Team.BLACK, Type.KNIGHT));
        CACHE.put(Team.WHITE, new Knight(Team.WHITE, Type.KNIGHT));
    }

    public Knight(Team team, Type type) {
        super(team, type);
    }

    public static Piece getInstance(Team team) {
        NullChecker.validateNotNull(team);
        return CACHE.get(team);
    }

    @Override
    public Set<Square> findMovableAreas(Square square, Map<Square, Piece> board,
        Set<CastlingSetting> castlingElements) {
        return getAllMovableArea(square).stream()
            .filter(s -> !(board.containsKey(s) && isSameTeam(board.get(s))))
            .collect(Collectors.toSet());
    }
}
