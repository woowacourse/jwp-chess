package wooteco.chess.dto;

import wooteco.chess.domain.piece.PieceMapper;
import wooteco.chess.domain.piece.Pieces;

import java.util.List;

public class PiecesResponseDto {
    private final List<PieceResponseDto> pieces;

    public PiecesResponseDto(Pieces originPieces) {
        this.pieces = PieceMapper.getInstance().createPiecesResponseDTO(originPieces);
    }

    public List<PieceResponseDto> getPieces() {
        return pieces;
    }
}
