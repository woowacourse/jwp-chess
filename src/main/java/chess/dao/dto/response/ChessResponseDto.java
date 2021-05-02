package chess.dao.dto.response;

public class ChessResponseDto {
    private final Long id;
    private final String pieceName;
    private final String piecePosition;

    public ChessResponseDto(final Long id, final String pieceName, final String piecePosition) {
        this.id = id;
        this.pieceName = pieceName;
        this.piecePosition = piecePosition;
    }

    public Long getId() {
        return id;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPiecePosition() {
        return piecePosition;
    }
}
