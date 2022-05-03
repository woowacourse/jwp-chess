package chess.dto;

public class BoardDto {

    private String position;
    private String piece;

    public BoardDto() {
    }

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
