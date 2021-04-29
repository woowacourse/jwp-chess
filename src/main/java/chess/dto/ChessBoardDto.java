package chess.dto;

import java.util.Map;

public class ChessBoardDto {

    private final Map<String, String> positionAndPieceName;

    public ChessBoardDto(final Map<String, String> positionAndPieceName) {
        this.positionAndPieceName = positionAndPieceName;
    }

    public Map<String, String> getPositionAndPieceName() {
        return positionAndPieceName;
    }
}
