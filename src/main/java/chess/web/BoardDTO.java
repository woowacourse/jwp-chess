package chess.web;

import chess.domain.score.ChessScore;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;

public class BoardDTO {

    private Map<String, Element> data;

    private BoardDTO() {}

    private static BoardDTO buildModel() {
        return new BoardDTO();
    }

    public static BoardDTO generateUpdatedDTO(Map<Position, Piece> original) {
        BoardDTO boardDTO = buildModel();
        Map<String, Element> model = new HashMap<>();
        for (Position position : original.keySet()) {
            model.put(position.getPosition(), Element.from(original.get(position)));
        }
        boardDTO.data = model;
        return boardDTO;
    }

    public Map<String, Element> getData() {
        return data;
    }
}
