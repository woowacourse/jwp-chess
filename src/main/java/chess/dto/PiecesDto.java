package chess.dto;

import chess.domain.piece.Pieces;
import java.util.List;
import java.util.stream.Collectors;

public class PiecesDto {

    private List<PieceDto> pieces;

    public PiecesDto() {
    }

    public PiecesDto(List<PieceDto> pieces) {
        this.pieces = pieces;
    }

    public PiecesDto(Pieces pieces) {
        this.pieces = toDto(pieces);
    }

    private List<PieceDto> toDto(Pieces chessmen) {
        return chessmen.getPieces().stream()
                .map(PieceDto::new)
                .collect(Collectors.toList());
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

}
