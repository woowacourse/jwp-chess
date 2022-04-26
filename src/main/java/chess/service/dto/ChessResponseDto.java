package chess.service.dto;

import java.util.ArrayList;
import java.util.List;

public class ChessResponseDto {

    private final List<PieceDto> pieces;
    private final String turn;
    private final String status;

    public ChessResponseDto(List<PieceDto> pieces, String turn, String status) {
        this.pieces = new ArrayList<>(pieces);
        this.turn = turn;
        this.status = status;
    }

    public List<PieceDto> getPieces() {
        return List.copyOf(pieces);
    }

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
