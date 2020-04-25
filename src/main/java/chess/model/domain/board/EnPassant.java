package chess.model.domain.board;

import chess.model.domain.exception.ChessException;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EnPassant {

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
        enPassantsToAfterSquares.remove(moveInfo.get(MoveOrder.TO));
        Square squareBefore = moveInfo.get(MoveOrder.FROM);
        if (enPassantsToAfterSquares.containsValue(squareBefore)) {
            enPassantsToAfterSquares.remove(enPassantsToAfterSquares.keySet().stream()
                .filter(
                    boardSquare -> enPassantsToAfterSquares.get(boardSquare) == squareBefore)
                .findFirst()
                .orElseThrow(IllegalAccessError::new));
        }
    }

    public void add(Piece piece, MoveInfo moveInfo) {
        if (isPawnMoveTwoRank(piece, moveInfo)) {
            Square betweenWhenJumpRank = getBetween(moveInfo);
            Square afterSquare = moveInfo.get(MoveOrder.TO);
            enPassantsToAfterSquares.put(betweenWhenJumpRank, afterSquare);
            return;
        }
        throw new ChessException("폰이 두 칸 전진하지 않아, 앙파상 룰을 적용할 수 없습니다.");
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

    public boolean hasOtherEnpassant(Square square, Team gameTurn) {
        return enPassantsToAfterSquares.containsKey(square)
            && !getPawnByRank(square).isSameTeam(gameTurn);
    }

    public Square getAfterSquare(Square enPassantSquare) {
        return enPassantsToAfterSquares.get(enPassantSquare);
    }

    public static Square getBetween(MoveInfo moveInfo) {
        if (isMoveTwoRank(moveInfo)) {
            Square squareFrom = moveInfo.get(MoveOrder.FROM);
            Square squareTo = moveInfo.get(MoveOrder.TO);
            int rankCompare = squareFrom.getRankCompare(squareTo);
            return squareFrom.getIncreasedSquare(0, rankCompare * -1);
        }
        throw new IllegalArgumentException("2칸 이동하지 않았습니다.");
    }

    private static boolean isMoveTwoRank(MoveInfo moveInfo) {
        return Math.abs(moveInfo.calculateRankDistance()) == 2;
    }

    public Set<Square> getEnPassants() {
        return enPassantsToAfterSquares.keySet();
    }
}
