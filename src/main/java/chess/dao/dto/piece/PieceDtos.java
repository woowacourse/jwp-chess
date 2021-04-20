package chess.dao.dto.piece;

import java.util.List;

public class PieceDtos {

    private final List<PieceDto> pieces;

    public PieceDtos(final List<PieceDto> pieces) {
        this.pieces = pieces;
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }
}
