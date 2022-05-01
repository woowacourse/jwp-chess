package chess.dto;

import chess.service.SquareRes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardRes {

    private Map<String, PieceRes> board;

    private BoardRes(Map<String, PieceRes> board) {
        this.board = board;
    }

    public static BoardRes createBoardResToListSquare(List<SquareRes> squares) {
        Map<String, PieceRes> pieces = new HashMap<>();
        for (SquareRes square : squares) {
            pieces.put(square.getPosition(), new PieceRes(square.getSymbol(), square.getColor()));
        }
        return new BoardRes(pieces);
    }

    public Map<String, PieceRes> getBoard() {
        return board;
    }
}
