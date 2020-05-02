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

public class Board {

    private final Map<Square, Piece> board;

    private Board(Map<Square, Piece> board) {
        this.board = new HashMap<>(board);
    }

    public static Board createInitial() {
        return new Board(BoardFactory.create());
    }

    public static Board of(Map<Square, Piece> chessBoard) {
        return new Board(chessBoard);
    }

    public static Board of(Board board) {
        return new Board(board.getBoard());
    }

    public static boolean isInitialPosition(Square square, Piece piece) {
        return (piece instanceof Pawn)
            && (square.isSameRank(Rank.SEVENTH) || square.isSameRank(Rank.SECOND));
    }

    public Optional<Square> findSquareForPromote() {
        return board.keySet().stream()
            .filter(boardSquare -> board.get(boardSquare) instanceof Pawn)
            .filter(Square::isLastRank)
            .findFirst();
    }

    public Piece removeBy(Square square) {
        return board.remove(square);
    }

    public void changePiece(Square square, Piece piece) {
        board.put(square, piece);
    }

    public boolean isNotExist(Square square) {
        return !board.containsKey(square);
    }

    public long countPieceOfKing() {
        return board.values().stream()
            .filter(piece -> piece instanceof King)
            .count();
    }

    public TeamScore deriveTeamScore() {
        return new TeamScore(board.values(), countPawnSameFileByTeam());
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
        return board.keySet().stream()
            .filter(square -> board.get(square) == Pawn.getInstance(team))
            .collect(Collectors.toList());
    }

    public Piece findPieceBy(Square square) {
        return board.get(square);
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

        Piece currentPiece = board.remove(moveInfoBefore);
        board.put(moveInfoAfter, currentPiece);
    }

    public void addElements(Map<Square, Piece> enPassantBoard) {
        for (Square square : enPassantBoard.keySet()) {
            board.put(square, enPassantBoard.get(square));
        }
    }

    public Map<Square, Piece> getBoard() {
        return board;
    }
}
