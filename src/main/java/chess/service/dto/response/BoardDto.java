package chess.service.dto.response;

import chess.model.board.Board;
import chess.model.board.Square;
import chess.model.piece.Piece;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardDto {
    private final List<PieceWithSquareDto> pieces;

    public BoardDto(Board board) {
        Map<Square, Piece> pieces = board.getValue();
        this.pieces = pieces.keySet().stream()
                .map(square -> new PieceWithSquareDto(square, pieces.get(square)))
                .collect(Collectors.toList());
    }

    public List<PieceWithSquareDto> getPieces() {
        return pieces;
    }
}
