package chess.dto;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ChessBoardDto {

    private final Map<String, Piece> board;

    private ChessBoardDto(Map<String, Piece> board) {
        this.board = board;
    }

    public static ChessBoardDto from(Map<Position, Piece> board) {
        Map<String, Piece> convertedBoard = new TreeMap<>();
        for (Position position : board.keySet()) {
            convertedBoard.put(position.stringName(), board.get(position));
        }
        return new ChessBoardDto(convertedBoard);
    }

    public Map<String, Piece> getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoardDto that = (ChessBoardDto) o;
        return Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
}
