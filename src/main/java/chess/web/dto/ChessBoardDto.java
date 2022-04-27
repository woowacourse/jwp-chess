package chess.web.dto;

public class ChessBoardDto {
    private String position;
    private String piece;

    public ChessBoardDto(String position, String piece) {
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
