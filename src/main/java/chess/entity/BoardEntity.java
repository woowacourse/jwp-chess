package chess.entity;

public class BoardEntity {

    private String position;
    private String piece;

    public BoardEntity(String position, String piece) {
        this.position = position;
        this.piece = piece;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }

    public void update(String piece) {
        this.piece = piece;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BoardEntity that = (BoardEntity) o;

        if (getPosition() != null ? !getPosition().equals(that.getPosition()) : that.getPosition() != null) {
            return false;
        }
        return getPiece() != null ? getPiece().equals(that.getPiece()) : that.getPiece() == null;
    }

    @Override
    public int hashCode() {
        int result = getPosition() != null ? getPosition().hashCode() : 0;
        result = 31 * result + (getPiece() != null ? getPiece().hashCode() : 0);
        return result;
    }
}
