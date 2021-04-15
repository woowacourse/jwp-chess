package chess.dto;

import java.util.Map;

public class ChessBoardDTO {

    private final Map<String, String> positionAndPieceName;

    public ChessBoardDTO(Map<String, String> positionAndPieceName) {
        this.positionAndPieceName = positionAndPieceName;
    }

    public Map<String, String> getPositionAndPieceName() {
        return positionAndPieceName;
    }
}
