package chess.dto;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardResponse {
    private final Map<String, PieceDto> board;

    private BoardResponse(Map<String, PieceDto> board) {
        this.board = board;
    }

    public static BoardResponse of(Map<Position, Piece> board) {
        return new BoardResponse(board.entrySet().stream()
                .collect(Collectors.toMap(
                                entry -> entry.getKey().toString(),
                                entry -> PieceDto.of(entry.getValue(), entry.getKey())
                        )
                ));
    }

    public Map<String, PieceDto> getBoard() {
        return board;
    }
}
