package chess.controller.dto.response;

import java.util.Map;

public class PlayerResponse {

    private final Map<PositionResponse, PieceResponse> pieceUnits;

    public PlayerResponse(final Map<PositionResponse, PieceResponse> pieceUnits) {
        this.pieceUnits = pieceUnits;
    }

    public Map<PositionResponse, PieceResponse> getPieceUnits() {
        return pieceUnits;
    }
}
