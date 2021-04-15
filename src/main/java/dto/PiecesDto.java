package dto;

import chess.domain.Position;
import chess.domain.piece.Piece;
import util.PieceConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PiecesDto {
    private List<PieceDto> pieces;

    private PiecesDto(List<PieceDto> pieces) {
       this.pieces = pieces;
    }

    public static PiecesDto of(Map<Position, Piece> pieces, String color) {
        List<PieceDto> pieceList = new ArrayList<>();

        for (Map.Entry<Position, Piece> piece : pieces.entrySet()) {
            pieceList.add(
                    new PieceDto(piece.getKey().getKey(),
                    PieceConverter.convertToPieceName(color, piece.getValue()))
            );
        }

        return new PiecesDto(pieceList);
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }
}
