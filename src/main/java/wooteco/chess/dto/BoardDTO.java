package wooteco.chess.dto;

public class BoardDTO {

    String piece;
    String position;

    public String getPiece() {
        return piece;
    }

    public void setPiece(final String piece) {
        this.piece = piece;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(final String position) {
        this.position = position;
    }
}
