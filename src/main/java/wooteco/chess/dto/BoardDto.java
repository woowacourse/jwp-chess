package wooteco.chess.dto;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.pieces.Pieces;
import wooteco.chess.domain.position.Position;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardDto {
    private final Map<Position, PieceDto> board = new HashMap<>();

    public BoardDto(Pieces pieces) {
        for (Piece piece : pieces.getPieces()) {
            board.put(piece.getPosition(), new PieceDto(piece));
        }
    }

    public Map<Position, PieceDto> getBoard() {
        return Collections.unmodifiableMap(board);
    }
}