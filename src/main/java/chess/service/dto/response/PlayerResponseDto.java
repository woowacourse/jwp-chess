package chess.service.dto.response;

import java.util.Map;

public class PlayerResponseDto {

    private final Map<PositionResponseDto, PieceResponseDto> pieceUnits;

    public PlayerResponseDto(final Map<PositionResponseDto, PieceResponseDto> pieceUnits) {
        this.pieceUnits = pieceUnits;
    }

    public Map<PositionResponseDto, PieceResponseDto> getPieceUnits() {
        return pieceUnits;
    }
}
