package chess.webdto;

public class PieceInfoDto {
    private String position;
    private String piece;

    public PieceInfoDto(String position, String piece) {
        this.position = position;
        this.piece = piece;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }
}
