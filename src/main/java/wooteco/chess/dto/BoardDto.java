package wooteco.chess.dto;

import java.util.Map;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;

public class BoardDto {
    Map<Position, Piece> board;

    public BoardDto(final Board board) {
        this.board = board.getBoard();
    }

    public Map<Position, Piece> getBoard() {
        return board;
    }
}
