package chess.dto;

public class BoardDto {
    private final String position;
    private final String piece;

    public BoardDto(String position, String piece) {
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
