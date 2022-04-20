package chess.entity;

public class BoardEntity {

    private final String position;
    private final String piece;

    public BoardEntity(final String position, final String piece) {
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
