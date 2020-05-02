package wooteco.chess.dto;

import java.util.List;

import wooteco.chess.domain.piece.PieceMapper;
import wooteco.chess.domain.piece.Pieces;

public class PiecesResponseDto {
    private final List<PieceResponseDto> pieces;

    public PiecesResponseDto(Pieces originPieces) {
        this.pieces = PieceMapper.getInstance().createPiecesResponseDTO(originPieces);
    }

    public List<PieceResponseDto> getPieces() {
        return pieces;
    }
}
