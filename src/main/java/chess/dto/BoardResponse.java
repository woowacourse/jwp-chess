package chess.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardResponse {

    private Map<String, PieceResponse> board;

    private BoardResponse(Map<String, PieceResponse> board) {
        this.board = board;
    }

    public static BoardResponse createBoardResToListSquare(List<SquareResponse> squares) {
        Map<String, PieceResponse> pieces = new HashMap<>();
        for (SquareResponse square : squares) {
            pieces.put(square.getPosition(), new PieceResponse(square.getSymbol(), square.getColor()));
        }
        return new BoardResponse(pieces);
    }

    public Map<String, PieceResponse> getBoard() {
        return board;
    }
}
