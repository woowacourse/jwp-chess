package chess.dto;

import chess.domain.piece.Piece;

import java.util.List;

public class PiecesDto {
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;

    public PiecesDto(final List<Piece> whitePieces, final List<Piece> blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }
}
