package chess.dto.response.web;

import chess.domain.board.position.Position;

public class PositionResponse {
    public static String from(Position position) {
        return position.getFile().text() + position.getRank().index();
    }
}
