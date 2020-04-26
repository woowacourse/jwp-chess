package chess.model.domain.board;

import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EnPassant {

    private static Set<Square> BLACK_EN_PASSANTS;
    private static Set<Square> WHITE_EN_PASSANTS;

    static {
        Set<Square> blackEnPassants = new HashSet<>();
        Set<Square> whiteEnPassants = new HashSet<>();
        for (char file = 'a'; file <= 'h'; file++) {
            blackEnPassants.add(Square.of(file + "7"));
            whiteEnPassants.add(Square.of(file + "2"));
        }
        BLACK_EN_PASSANTS = Collections.unmodifiableSet(blackEnPassants);
        WHITE_EN_PASSANTS = Collections.unmodifiableSet(whiteEnPassants);
    }

    private Map<Square, Square> enPassantsToAfterSquares;

    public EnPassant() {
        this(new HashMap<>());
    }

    public EnPassant(Map<Square, Square> enPassantsToAfterSquares) {
        this.enPassantsToAfterSquares = enPassantsToAfterSquares;
    }

    public static boolean isPawnMoveTwoRank(Piece piece, MoveInfo moveInfo) {
        return piece instanceof Pawn && isMoveTwoRank(moveInfo);
    }

    public void removeEnPassant(MoveInfo moveInfo) {
        enPassantsToAfterSquares.remove(moveInfo.getTarget());
        Square squareBefore = moveInfo.getSource();
        if (enPassantsToAfterSquares.containsValue(squareBefore)) {
            enPassantsToAfterSquares.remove(enPassantsToAfterSquares.keySet().stream()
                .filter(
                    boardSquare -> enPassantsToAfterSquares.get(boardSquare) == squareBefore)
                .findFirst()
                .orElseThrow(IllegalAccessError::new));
        }
    }

    public void removeEnPassant(Team gameTurn) {
        if (gameTurn == Team.BLACK) {
            remove(BLACK_EN_PASSANTS);
            return;
        }
        remove(WHITE_EN_PASSANTS);
    }

    private void remove(Set<Square> deleteElements) {
        for (Square deleteElement : deleteElements) {
            enPassantsToAfterSquares.remove(deleteElement);
        }
    }

    public void add(Piece piece, MoveInfo moveInfo) {
        if (isPawnMoveTwoRank(piece, moveInfo)) {
            Square betweenWhenJumpRank = getBetween(moveInfo);
            Square afterSquare = moveInfo.getTarget();
            enPassantsToAfterSquares.put(betweenWhenJumpRank, afterSquare);
            return;
        }
        throw new IllegalArgumentException("폰이 두 칸 전진하지 않아, 앙파상 룰을 적용할 수 없습니다.");
    }

    public Map<Square, Piece> getEnPassantBoard(Team team) {
        if (enPassantsToAfterSquares.isEmpty()) {
            return new HashMap<>();
        }
        return enPassantsToAfterSquares.keySet().stream()
            .filter(boardSquare -> !getPawnByRank(boardSquare).isSameTeam(team))
            .collect(Collectors.toMap(boardSquare -> boardSquare, this::getPawnByRank));
    }

    private Piece getPawnByRank(Square square) {
        if (square.isSameRank(Rank.THIRD)) {
            return Pawn.getInstance(Team.WHITE);
        }
        if (square.isSameRank(Rank.SIXTH)) {
            return Pawn.getInstance(Team.BLACK);
        }
        throw new IllegalArgumentException("앙파상 Rank가 아닙니다.");
    }

    public boolean isEnemyPast(Square square, Team gameTurn) {
        return enPassantsToAfterSquares.containsKey(square) && !getPawnByRank(square)
            .isSameTeam(gameTurn);
    }

    public Square getCurrentSquare(Square enPassantSquare) {
        return enPassantsToAfterSquares.get(enPassantSquare);
    }

    public static Square getBetween(MoveInfo moveInfo) {
        if (isMoveTwoRank(moveInfo)) {
            Square squareFrom = moveInfo.getSource();
            Square squareTo = moveInfo.getTarget();
            int rankCompare = squareFrom.getRankCompare(squareTo);
            return squareFrom.getIncreasedSquare(0, rankCompare * -1);
        }
        throw new IllegalArgumentException("2칸 이동하지 않았습니다.");
    }

    private static boolean isMoveTwoRank(MoveInfo moveInfo) {
        return Math.abs(moveInfo.calculateRankDistance()) == 2;
    }

    public Set<Square> getEnPassantsKeys() {
        return enPassantsToAfterSquares.keySet();
    }

    public Map<Square, Square> getEnPassants() {
        return enPassantsToAfterSquares;
    }
}
