package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;

import java.util.Map;

public class Board {
    private final Map<Position, Piece> board;

    public Board(Map<Position, Piece> board) {
        this.board = board;
    }

    public void move(Position start, Position end) {

    }
}
