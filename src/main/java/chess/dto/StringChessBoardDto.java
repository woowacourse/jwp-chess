package chess.dto;

import java.util.Map;

public class StringChessBoardDto {
    private final Map<String, String> stringChessBoard;

    public StringChessBoardDto(final Map<String, String> stringChessBoard) {
        this.stringChessBoard = stringChessBoard;
    }

    public Map<String, String> getStringChessBoard() {
        return stringChessBoard;
    }
}
