package chess.dto.response;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.HashMap;
import java.util.Map;

public class BoardDto {
    private final Map<String, String> value;

    public BoardDto(Board board) {
        Map<String, String> rawBoard = new HashMap<>();
        for (Map.Entry<Position, Piece> entrySet : board.getValue().entrySet()) {
            String coordinate = entrySet.getKey().toCoordinate();
            String fullPieceName = entrySet.getValue().generateFullName();
            rawBoard.put(coordinate, fullPieceName);
        }

        this.value = rawBoard;
    }

    public Map<String, String> getValue() {
        return value;
    }
}
