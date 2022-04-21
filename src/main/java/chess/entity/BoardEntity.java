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

}
