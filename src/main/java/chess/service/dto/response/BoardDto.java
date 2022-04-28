package chess.service.dto.response;

import chess.entity.PieceEntity;
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
