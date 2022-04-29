package chess.controller.dto.response;

public class PieceResponse {

    private final String piece;

    public PieceResponse(final String piece) {
        this.piece = piece;
    }

    public String getPiece() {
        return piece;
    }
}
