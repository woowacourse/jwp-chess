package chess.dto;

public class PieceDto {
    private final String pieceName;
    private final String piecePosition;

    public PieceDto(final String pieceName, final String piecePosition) {
        this.pieceName = pieceName;
        this.piecePosition = piecePosition;
    }

    public String getName() {
        return pieceName;
    }

    public String getPosition() {
        return piecePosition;
    }
}
