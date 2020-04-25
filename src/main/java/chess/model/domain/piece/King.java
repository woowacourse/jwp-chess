package chess.model.domain.piece;

import chess.model.domain.board.Square;
import chess.model.domain.board.CastlingSetting;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import util.NullChecker;

public class King extends OneTimeMovePiece {

    private final static Map<Team, Piece> CACHE = new HashMap<>();

    static {
        CACHE.put(Team.BLACK, new King(Team.BLACK, Type.KING));
        CACHE.put(Team.WHITE, new King(Team.WHITE, Type.KING));
    }

    public King(Team team, Type type) {
        super(team, type);
    }

    public static Piece getPieceInstance(Team team) {
        NullChecker.validateNotNull(team);
        return CACHE.get(team);
    }

    @Override
    public Set<Square> getMovableArea(Square square, Map<Square, Piece> board,
        Set<CastlingSetting> castlingElements) {
        Set<Square> movableArea = getAllMovableArea(square).stream()
            .filter(s -> !(board.containsKey(s) && isSameTeam(board.get(s))))
            .collect(Collectors.toSet());
        movableArea.addAll(getCastlingMovableArea(square, board, castlingElements));
        return movableArea;
    }

    private Set<Square> getCastlingMovableArea(Square square,
        Map<Square, Piece> board, Set<CastlingSetting> castlingElements) {
        Set<CastlingSetting> sameColorCastlingElements = castlingElements.stream()
            .filter(castlingElement -> castlingElement.isSameColor(this))
            .collect(Collectors.toSet());
        Set<Square> castlingMovableAreas = CastlingSetting
            .getCastlingMovableAreas(sameColorCastlingElements);

        Set<Square> totalMovableArea = new HashSet<>();
        for (Square castlingMovableArea : castlingMovableAreas) {
            int fileCompare = square.getFileCompare(castlingMovableArea);
            Set<Square> squaresForCastling = IntStream
                .range(1, Square.MAX_FILE_AND_RANK_COUNT)
                .filter(index -> square.hasIncreased(fileCompare * -index, 0))
                .mapToObj(index -> square.getIncreased(fileCompare * -index, 0))
                .collect(Collectors.toSet());
            if (squaresForCastling.size() == getNonContains(board, squaresForCastling)) {
                totalMovableArea.add(castlingMovableArea);
            }
        }
        return totalMovableArea;
    }

    private int getNonContains(Map<Square, Piece> board,
        Set<Square> squaresForCastling) {
        return (int) squaresForCastling.stream()
            .filter(square -> !board.containsKey(square))
            .count() + 1;
    }
}
