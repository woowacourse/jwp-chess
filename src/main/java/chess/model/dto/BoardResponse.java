package chess.model.dto;

import chess.model.board.Board;
import chess.model.dao.PieceDao;

import java.util.Map;
import java.util.stream.Collectors;

public class BoardResponse {
    private Map<String, String> webBoard;

    public BoardResponse(Map<String, String> webBoard) {
        this.webBoard = webBoard;
    }

    public static BoardResponse from(Board board) {
        Map<String, String> webBoard = board.getBoard()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getPosition(), entry -> PieceDao.getPieceName(entry.getValue())));

        return new BoardResponse(webBoard);
    }

    public Map<String, String> getWebBoard() {
        return webBoard;
    }
}
