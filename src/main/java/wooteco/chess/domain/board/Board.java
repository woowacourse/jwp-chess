package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Map<Position, Piece> board = new HashMap<>();

    public Board() {
        this(BoardInitializer.initializeAll());
    }

    public Board(Map<Position, Piece> board) {
        this.board.clear();
        this.board.putAll(board);
    }

    public void updateBoard(Position sourcePosition, Position targetPosition) {
        Piece selectedPiece = this.board.get(sourcePosition);
        this.board.put(targetPosition, selectedPiece);
        this.board.remove(sourcePosition);
    }

    public void clear() {
        board.clear();
    }

    public Team checkWinner() {
        if (hasWhiteKing() && !hasBlackKing()) {
            return Team.WHITE;
        }
        if (!hasWhiteKing() && hasBlackKing()) {
            return Team.BLACK;
        }
        return Team.NONE;
    }

    private boolean hasWhiteKing() {
        return board.values()
                .stream()
                .anyMatch(Piece::isWhiteKing);
    }

    private boolean hasBlackKing() {
        return board.values()
                .stream()
                .anyMatch(Piece::isBlackKing);
    }

    public boolean isEmpty(final Position position) {
        return !this.board.containsKey(position);
    }

    public Piece getPiece(final Position position) {
        return this.board.get(position);
    }

    public Map<Position, Piece> get() {
        return board;
    }
}