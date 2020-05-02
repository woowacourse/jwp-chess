package chess.model.domain.board;

import chess.model.domain.piece.King;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChessBoard {

    private final Map<Square, Piece> chessBoard;

    private ChessBoard(Map<Square, Piece> chessBoard) {
        this.chessBoard = new HashMap<>(chessBoard);
    }

    public static ChessBoard createInitial() {
        return new ChessBoard(new BoardFactory().create());
    }

    public static ChessBoard of(Map<Square, Piece> chessBoard) {
        return new ChessBoard(chessBoard);
    }

    public static ChessBoard of(ChessBoard chessBoard) {
        return new ChessBoard(chessBoard.getChessBoard());
    }

    public static boolean isInitialPoint(Square square, Piece piece) {
        return (piece instanceof Pawn)
            && (square.isSameRank(Rank.SEVENTH) || square.isSameRank(Rank.SECOND));
    }

    public Optional<Square> findSquareForPromote() {
        return chessBoard.keySet().stream()
            .filter(boardSquare -> chessBoard.get(boardSquare) instanceof Pawn)
            .filter(Square::isLastRank)
            .findFirst();
    }

    public Piece removeBy(Square square) {
        return chessBoard.remove(square);
    }

    public void changePiece(Square square, Piece piece) {
        chessBoard.put(square, piece);
    }

    public Piece findPieceBy(Square square) {
        return chessBoard.get(square);
    }

    public boolean isNotExist(Square square) {
        return !chessBoard.containsKey(square);
    }

    public long countPieceOfKing() {
        return chessBoard.values().stream()
            .filter(piece -> piece instanceof King)
            .count();
    }

    public TeamScore deriveTeamScore() {
        return new TeamScore(chessBoard.values(), countPawnSameFileByTeam());
    }

    private Map<Team, Integer> countPawnSameFileByTeam() {
        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        for (Team team : Team.values()) {
            List<Square> pawnSquare = findPawnSquaresOf(team);
            pawnSameFileCountByTeam.put(team, countSameFile(pawnSquare));
        }
        return pawnSameFileCountByTeam;
    }

    private List<Square> findPawnSquaresOf(Team team) {
        return chessBoard.keySet().stream()
            .filter(square -> chessBoard.get(square) == Pawn.getInstance(team))
            .collect(Collectors.toList());
    }

    private int countSameFile(List<Square> pawnSquare) {
        int count = 0;
        for (Square boardSquare : pawnSquare) {
            count += pawnSquare.stream()
                .filter(square -> boardSquare.isSameFile(square) && boardSquare != square)
                .count();
        }
        return count;
    }

    public void move(MoveInfo moveInfo) {
        Square moveInfoBefore = moveInfo.getSource();
        Square moveInfoAfter = moveInfo.getTarget();

        Piece currentPiece = chessBoard.remove(moveInfoBefore);
        chessBoard.put(moveInfoAfter, currentPiece);
    }

    public void addElements(Map<Square, Piece> enPassantBoard) {
        for (Square square : enPassantBoard.keySet()) {
            chessBoard.put(square, enPassantBoard.get(square));
        }
    }

    public Map<Square, Piece> getChessBoard() {
        return chessBoard;
    }
}
