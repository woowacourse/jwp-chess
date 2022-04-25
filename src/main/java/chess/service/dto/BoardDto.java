package chess.service.dto;

import chess.dao.PieceEntity;
import java.util.List;

public class BoardDto {
    private final List<PieceEntity> pieces;

    public BoardDto(List<PieceEntity> pieces) {
        this.pieces = pieces;
    }

    public List<PieceEntity> getPieces() {
        return pieces;
    }
}
