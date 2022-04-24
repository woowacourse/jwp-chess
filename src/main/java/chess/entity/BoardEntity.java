package chess.entity;

public class BoardEntity {

    private Long id;
    private Long roomId;
    private String position;
    private String piece;

    public BoardEntity(final Long id, final Long roomId, final String position, final String piece) {
        this.id = id;
        this.roomId = roomId;
        this.position = position;
        this.piece = piece;
    }

    public BoardEntity(final Long roomId, final String position, final String piece) {
        this.roomId = roomId;
        this.position = position;
        this.piece = piece;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
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

        if (getRoomId() != null ? !getRoomId().equals(that.getRoomId()) : that.getRoomId() != null) {
            return false;
        }
        if (getPosition() != null ? !getPosition().equals(that.getPosition()) : that.getPosition() != null) {
            return false;
        }
        return getPiece() != null ? getPiece().equals(that.getPiece()) : that.getPiece() == null;
    }

    @Override
    public int hashCode() {
        int result = getRoomId() != null ? getRoomId().hashCode() : 0;
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        result = 31 * result + (getPiece() != null ? getPiece().hashCode() : 0);
        return result;
    }
}
