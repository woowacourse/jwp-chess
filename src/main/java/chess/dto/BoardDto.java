package chess.dto;

import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.HashMap;
import java.util.Map;

public class BoardDto {

    private Map<String, String> values;

    private BoardDto(Map<String, String> values) {
        this.values = values;
    }

    public static BoardDto from(final Map<String, String> squares) {
        return new BoardDto(squares);
    }

    public Map<String, String> getValues() {
        return values;
    }
}
