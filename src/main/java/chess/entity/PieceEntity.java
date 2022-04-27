package chess.entity;

public class PieceEntity {

    private final Long id;
    private final Long roomId;
    private final String position;
    private final String type;
    private final String color;

    public PieceEntity(final Long roomId, final String position, final String type, final String color) {
        this(null, roomId, position, type, color);
    }

    public PieceEntity(final Long id, final Long roomId, final String position, final String type, final String color) {
        this.id = id;
        this.roomId = roomId;
        this.position = position;
        this.type = type;
        this.color = color;
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

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}
