package chess.domain;

import chess.domain.board.Board;
import chess.domain.board.Position;

public class ChessGame {
    private final Long id;
    private final Board board;
    private final Room room;

    public ChessGame(Long id, Board board, Room room) {
        this.id = id;
        this.board = board;
        this.room = room;
    }

    public ChessGame(Long id, Board board) {
        this(id, board, null);
    }

    private ChessGame(Board board, Room room) {
        this(null, board, room);
    }

    public static ChessGame create(Room room) {
        return new ChessGame(new Board(), room);
    }

    public void move(Position beforePosition, Position afterPosition) {
        board.move(beforePosition, afterPosition);
    }

    public boolean hasKingCaptured() {
        return board.hasKingCaptured();
    }

    public double scoreOfWhite() {
        return board.scoreOfWhite();
    }

    public double scoreOfBlack() {
        return board.scoreOfBlack();
    }

    public Winner findWinner() {
        if (board.hasBlackKingCaptured()) {
            return Winner.WHITE;
        }
        if (board.hasWhiteKingCaptured()) {
            return Winner.BLACK;
        }
        return findWinnerByScore();
    }

    private Winner findWinnerByScore() {
        final int compared = Double.compare(board.scoreOfBlack(), board.scoreOfWhite());
        if (compared > 0) {
            return Winner.BLACK;
        }
        if (compared < 0) {
            return Winner.WHITE;
        }
        return Winner.DRAW;
    }

    public Long getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public Room getRoom() {
        return room;
    }
}
