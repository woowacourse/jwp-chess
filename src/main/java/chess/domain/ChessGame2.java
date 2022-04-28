package chess.domain;

import chess.domain.board.Board;
import chess.domain.board.Position;

public class ChessGame2 {
    private final Long id;
    private final Board board;
    private final Room room;

    public ChessGame2(Long id, Board board, Room room) {
        this.id = id;
        this.board = board;
        this.room = room;
    }

    public ChessGame2(Long id, Board board) {
        this(id, board, null);
    }

    private ChessGame2(Board board, Room room) {
        this(null, board, room);
    }

    public static ChessGame2 create(Room room) {
        return new ChessGame2(new Board(), room);
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
