package wooteco.chess.dto;

import wooteco.chess.domain.piece.PieceMapper;
import wooteco.chess.domain.piece.Pieces;

import java.util.List;

public class PiecesResponseDTO {
    List<PieceResponseDTO> pieces;

    public PiecesResponseDTO(Pieces originPieces) {
        this.pieces = PieceMapper.getInstance().createPiecesResponseDTO(originPieces);
    }

    public List<PieceResponseDTO> getPieces() {
        return pieces;
    }
}
