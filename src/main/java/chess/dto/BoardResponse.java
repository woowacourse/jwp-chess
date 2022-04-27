package chess.dto;

import chess.domain.board.Point;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardResponse {

    private final List<PieceResponse> pieceResponses;

    public BoardResponse(List<PieceResponse> pieceResponses) {
        this.pieceResponses = pieceResponses;
    }

    public static BoardResponse of(Map<Point, Piece> pointPieces) {
        return new BoardResponse(
            pointPieces.entrySet()
                .stream()
                .map(entry -> PieceResponse.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList())
        );
    }
}
