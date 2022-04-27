package chess.dto.response;

import chess.domain.chesspiece.Color;
import chess.domain.position.Position;

public class ChessPieceDto {

    private final String position;
    private final String pieceType;
    private final String color;

    private ChessPieceDto(final String position, final String pieceType, final String color) {
        this.position = position;
        this.pieceType = pieceType;
        this.color = color;
    }

    public static ChessPieceDto of(final Position position, final String chessPiece, final Color color) {
        return new ChessPieceDto(
                position.getValue(),
                chessPiece,
                color.getValue()
        );
    }

    public String getPosition() {
        return position;
    }

    public String getPieceType() {
        return pieceType;
    }

    public String getColor() {
        return color;
    }
}
