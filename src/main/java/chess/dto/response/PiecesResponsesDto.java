package chess.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public class PiecesResponsesDto {

    private final List<PieceResponseDto> pieceResponseDtos;

    public PiecesResponsesDto(Map<Position, Piece> board) {
        this.pieceResponseDtos = new ArrayList<>();
        for (Map.Entry<Position, Piece> piece : board.entrySet()) {
            pieceResponseDtos.add(new PieceResponseDto(piece.getKey().chessCoordinate(), piece.getValue().getName()));
        }
    }

    public List<PieceResponseDto> getPieceResponseDtos() {
        return pieceResponseDtos;
    }
}
