package chess.model.domain.board;

import chess.model.domain.piece.King;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChessBoard {

    private Map<Square, Piece> chessBoard;

    public ChessBoard(Map<Square, Piece> chessBoard) {
        this.chessBoard = chessBoard;
    }

    public static boolean isInitialPoint(Square square, Piece piece) {
        return (piece instanceof Pawn)
            && (square.isSameRank(Rank.SEVENTH) || square.isSameRank(Rank.SECOND));
    }

    public static ChessBoard of(Map<Square, Piece> chessBoard) {
        return new ChessBoard(chessBoard);
    }

    public static ChessBoard create() {
        return new ChessBoard(new BoardInitial().getInitialize());
    }

    public Optional<Square> findSquareForPromote() {
        return chessBoard.keySet().stream()
            .filter(boardSquare -> chessBoard.get(boardSquare) instanceof Pawn)
            .filter(Square::isLastRank)
            .findFirst();
    }

    public Piece remove(Square square) {
        return chessBoard.remove(square);
    }

    public void put(Square square, Piece piece) {
        chessBoard.put(square, piece);
    }

    public Piece get(Square square) {
        return chessBoard.get(square);
    }

    public boolean hasNot(Square square) {
        return !chessBoard.containsKey(square);
    }

    public long countPieceOfKing() {
        return chessBoard.values().stream()
            .filter(piece -> piece instanceof King)
            .count();
    }

    public TeamScore getTeamScore() {
        return new TeamScore(chessBoard.values(), countPawnSameFileByTeam());
    }

    private Map<Team, Integer> countPawnSameFileByTeam() {
        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        for (Team team : Team.values()) {
            List<Square> pawnSquare = getPawnSquaresOf(team);
            pawnSameFileCountByTeam.put(team, getCountSameFile(pawnSquare));
        }
        return pawnSameFileCountByTeam;
    }

    private List<Square> getPawnSquaresOf(Team team) {
        return chessBoard.keySet().stream()
            .filter(square -> chessBoard.get(square) == Pawn.getInstance(team))
            .collect(Collectors.toList());
    }

    private int getCountSameFile(List<Square> pawnSquare) {
        int count = 0;
        for (Square boardSquare : pawnSquare) {
            count += pawnSquare.stream()
                .filter(square -> boardSquare.isSameFile(square) && boardSquare != square)
                .count();
        }
        return count;
    }

    public Map<Square, Piece> getChessBoard() {
        return chessBoard;
    }
}
