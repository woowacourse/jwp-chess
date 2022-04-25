package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public final class BoardElementDto {

    private String pieceName;
    private String pieceColor;
    private String position;

    public BoardElementDto(Position position, Piece piece) {
        this.pieceName = piece.getNotation().name();
        this.pieceColor = piece.getColor().name();
        this.position = position.getFile().name() + position.getRankNumber();
    }

    public BoardElementDto(String pieceName, String pieceColor, String position) {
        this.pieceName = pieceName;
        this.pieceColor = pieceColor;
        this.position = position;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public String getPosition() {
        return position;
    }
}
