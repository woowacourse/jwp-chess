package chess.service.dto.response;

public class PieceResponseDto {

    private final String piece;

    public PieceResponseDto(final String piece) {
        this.piece = piece;
    }

    public String getPiece() {
        return piece;
    }
}
