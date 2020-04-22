package wooteco.chess.domain;

import wooteco.chess.domain.board.Board;

public class Room {
    private final int number;
    private Board board;

    public Room(int number, Board board) {
        this.number = number;
        this.board = board;
    }
}
