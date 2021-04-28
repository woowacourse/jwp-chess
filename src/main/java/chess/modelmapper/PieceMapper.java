package chess.modelmapper;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.dto.PieceDto;

import java.util.ArrayList;
import java.util.List;

public class PieceMapper {
    public static List<Piece> PiecesDtoGroupConvertToPieces(List<PieceDto> piecesDto) {
        List<Piece> pieces = new ArrayList<>();
        for (PieceDto pieceDto : piecesDto) {
            Color color = null;
            if (pieceDto.getIsBlack()) {
                color = Color.BLACK;
            }
            if (!pieceDto.getIsBlack()) {
                color = Color.WHITE;
            }
            Piece piece = PieceFactory.from(
                    pieceDto.getName().charAt(0),
                    color, pieceDto.getPosition().charAt(0),
                    pieceDto.getPosition().charAt(1)
            );
            pieces.add(piece);
        }
        return pieces;
    }
}
