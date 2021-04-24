package chess.controller.dto;

import chess.domain.location.Position;
import chess.domain.piece.Piece;

import java.util.HashMap;
import java.util.Map;

public class BoardDto {
    private final Map<String, String> maps;

    public BoardDto(Map<Position, Piece> board) {
        maps = new HashMap<>();
        for (Position position : board.keySet()) {
            maps.put(position.toKey(), board.get(position).display());
        }
    }

    public Map<String, String> getMaps() {
        return maps;
    }
}
