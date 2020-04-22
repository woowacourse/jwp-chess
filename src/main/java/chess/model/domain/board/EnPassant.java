package chess.model.domain.board;

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

    public static boolean isPawnJump(Piece piece, MoveInfo moveInfo) {
        return piece instanceof Pawn && isJumpRank(moveInfo);
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

    public void addIfPawnJump(Piece piece, MoveInfo moveInfo) {
        if (isPawnJump(piece, moveInfo)) {
            Square betweenWhenJumpRank = getBetween(moveInfo);
            Square afterSquare = moveInfo.get(MoveOrder.TO);
            enPassantsToAfterSquares.put(betweenWhenJumpRank, afterSquare);
        }
    }

    public Map<Square, Piece> getEnPassantBoard(Team team) {
        if (enPassantsToAfterSquares.isEmpty()) {
            return new HashMap<>();
        }
        return enPassantsToAfterSquares.keySet().stream()
            .filter(boardSquare -> !getRankByPawn(boardSquare).isSameTeam(team))
            .collect(Collectors.toMap(boardSquare -> boardSquare, this::getRankByPawn));
    }

    private Piece getRankByPawn(Square square) {
        if (square.isSameRank(Rank.THIRD)) {
            return Pawn.getPieceInstance(Team.WHITE);
        }
        if (square.isSameRank(Rank.SIXTH)) {
            return Pawn.getPieceInstance(Team.BLACK);
        }
        throw new IllegalArgumentException("인자 오류");
    }

    public Set<Square> getEnPassants() {
        return enPassantsToAfterSquares.keySet();
    }

    public boolean hasOtherEnpassant(Square square, Team gameTurn) {
        return enPassantsToAfterSquares.containsKey(square)
            && !getRankByPawn(square).isSameTeam(gameTurn);
    }

    public Square getAfterSquare(Square enPassantSquare) {
        return enPassantsToAfterSquares.get(enPassantSquare);
    }

    public static Square getBetween(MoveInfo moveInfo) {
        if (isJumpRank(moveInfo)) {
            Square squareFrom = moveInfo.get(MoveOrder.FROM);
            Square squareTo = moveInfo.get(MoveOrder.TO);
            int rankCompare = squareFrom.getRankCompare(squareTo);
            return squareFrom.getIncreased(0, rankCompare * -1);
        }
        throw new IllegalArgumentException("JUMP RANK가 아닙니다.");
    }

    private static boolean isJumpRank(MoveInfo moveInfo) {
        Square squareFrom = moveInfo.get(MoveOrder.FROM);
        Square squareTo = moveInfo.get(MoveOrder.TO);
        return Math.abs(squareFrom.calculateRankDistance(squareTo)) == 2;
    }
}
