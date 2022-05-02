package chess.dto.response;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.position.XAxis;
import chess.domain.position.YAxis;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BoardDto {
    private static final String PIECE_NAME_FORMAT = "%s_%s";

    private final Map<String, String> value;

    public BoardDto(Board board) {
        Map<String, String> rawBoard = new HashMap<>();
        for (Map.Entry<Position, Piece> entrySet : board.getValue().entrySet()) {
            String coordinate = generatePositionName(entrySet.getKey());
            String fullPieceName = generatePieceName(entrySet.getValue());
            rawBoard.put(coordinate, fullPieceName);
        }

        this.value = rawBoard;
    }

    private String generatePositionName(Position position) {
        XAxis xAxis = position.getXAxis();
        YAxis yAxis = position.getYAxis();
        return xAxis.name().toLowerCase(Locale.ROOT) + yAxis.getValue();
    }

    private String generatePieceName(Piece piece) {
        String pieceName = piece.getPieceType().name();
        String pieceColorName = piece.getPieceColor().name();
        return String.format(PIECE_NAME_FORMAT, pieceName, pieceColorName);
    }


    public Map<String, String> getValue() {
        return value;
    }
}
