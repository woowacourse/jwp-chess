package chess.dto.response;

public class PieceResponseDto {
    private final String position;
    private final String name;

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
//
//    public PieceResponseDto(final Position position, final Piece piece) {
//        this.position = position.chessCoordinate();
//        this.name = piece.getName();
//    }

    public PieceResponseDto(final String position, final String pieceName) {
        this.position = position;
        this.name = pieceName;
    }
}
