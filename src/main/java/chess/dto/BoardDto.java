package chess.dto;

import java.util.Map;

public class BoardDto {

    private Map<String, PieceDto> board;

    public BoardDto(Map<String, PieceDto> board) {
        this.board = board;
    }

    public Map<String, PieceDto> getBoard() {
        return board;
    }
}
