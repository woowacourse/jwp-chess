package chess.dto.response.web;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class PieceResponse {

    private final Map<String, String> value;

    public PieceResponse(Position position, Piece piece) {
        this(positionText(position), piece);
    }

    public PieceResponse(String position, Piece piece) {
        Map<String, String> value = new HashMap<>();

        value.put("position", position);
        value.put("piece", piece.getClass().getSimpleName());
        value.put("team", piece.teamName().toLowerCase());

        this.value = value;
    }

    public static String positionText(Position position) {
        return position.getFile().text() + position.getRank().index();
    }
}
