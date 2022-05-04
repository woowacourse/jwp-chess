package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import java.util.ArrayList;
import java.util.List;

public class PiecesDto {

    private List<PieceDto> pieces;

    public PiecesDto() {
    }

    private PiecesDto(List<PieceDto> pieces) {
        this.pieces = pieces;
    }

    public static PiecesDto toDto(Pieces pieces) {
        List<PieceDto> pieceDtos = new ArrayList<>();
        for (Piece piece : pieces.getPieces()) {
            pieceDtos.add(new PieceDto(piece.getPosition().getPosition(),
                piece.getColor().getName(),
                piece.getName()));
        }
        return new PiecesDto(pieceDtos);
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

}
