package chess.dto.response;

import chess.dto.PieceDto;
import chess.dto.PiecesDto;
import java.util.ArrayList;
import java.util.List;

public class PiecesResponsesDto {

    private final List<PieceResponseDto> pieceResponseDtos;

    public PiecesResponsesDto(PiecesDto piecesDto) {
        this.pieceResponseDtos = new ArrayList<>();
        for (PieceDto pieceDto : piecesDto.getPieceDtos()) {
            pieceResponseDtos.add(new PieceResponseDto(pieceDto));
        }
    }

    public List<PieceResponseDto> getPieceResponseDtos() {
        return pieceResponseDtos;
    }
}
