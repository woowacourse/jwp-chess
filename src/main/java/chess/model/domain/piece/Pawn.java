package chess.model.domain.piece;

import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.Square;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import util.NullChecker;

public class Pawn extends OneTimeMovePiece {

    private final static Map<Team, Piece> CACHE = new HashMap<>();

    static {
        CACHE.put(Team.BLACK, new Pawn(Team.BLACK, Type.PAWN));
        CACHE.put(Team.WHITE, new Pawn(Team.WHITE, Type.PAWN));
    }

    public Pawn(Team team, Type type) {
        super(team, type);
    }

    public static Piece getPieceInstance(Team team) {
        NullChecker.validateNotNull(team);
        return CACHE.get(team);
    }

    @Override
    public Set<Square> getMovableArea(Square square, Map<Square, Piece> board,
        Set<CastlingSetting> castlingElements) {
        Set<Square> allMovableArea = getAllMovableArea(square);
        Set<Square> containsMovableArea = allMovableArea.stream()
            .filter(movableArea -> !board.containsKey(movableArea))
            .collect(Collectors.toSet());
        Set<Square> totalMovableArea = new HashSet<>();
        totalMovableArea.addAll(getStraightMovableArea(square, board, containsMovableArea));
        totalMovableArea.addAll(getDiagonalMovableArea(board, allMovableArea));
        return totalMovableArea;
    }

    private Set<Square> getStraightMovableArea(Square square,
        Map<Square, Piece> board, Set<Square> containsMovableArea) {
        Set<Square> straightMovableArea = new HashSet<>();
        for (Square movableArea : containsMovableArea) {
            if (movableArea.hasIncreased(0, movableArea.getRankCompare(square))) {
                Square oneMore = movableArea
                    .getIncreased(0, movableArea.getRankCompare(square));
                straightMovableArea.addAll(getFrontMovableArea(square, board, oneMore));
            }
            straightMovableArea.add(movableArea);
        }
        return straightMovableArea;
    }

    private Set<Square> getFrontMovableArea(Square square,
        Map<Square, Piece> board, Square oneMore) {
        Set<Square> frontMovableArea = new HashSet<>();
        boolean initialPoint = ChessGame.isInitialPoint(square, this);
        boolean containsOneMore = !board.containsKey(oneMore);
        if (initialPoint && containsOneMore) {
            frontMovableArea.add(oneMore);
        }
        return frontMovableArea;
    }

    private Set<Square> getDiagonalMovableArea(Map<Square, Piece> board,
        Set<Square> allMovableArea) {
        Set<Square> diagonalMovableArea = new HashSet<>();
        for (Square movableArea : allMovableArea) {
            if (movableArea.hasIncreased(-1, 0)) {
                diagonalMovableArea.add(movableArea.getIncreased(-1, 0));
            }
            if (movableArea.hasIncreased(1, 0)) {
                diagonalMovableArea.add(movableArea.getIncreased(1, 0));
            }
        }
        return diagonalMovableArea.stream()
            .filter(board::containsKey)
            .filter(movableArea -> !isSameTeam(board.get(movableArea)))
            .collect(Collectors.toSet());
    }
}
