package chess.dto.response;

import java.util.List;


public class PiecesResponseDto {

    private List<PieceResponseDto> alivePieces;

    public PiecesResponseDto(List<PieceResponseDto> alivePieces) {
        this.alivePieces = alivePieces;
    }

    public List<PieceResponseDto> getAlivePieces() {
        return alivePieces;
    }
}
