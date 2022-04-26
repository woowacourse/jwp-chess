package chess.entity;

public class ChessPiece {

    private final int id;
    private final int roomId;
    private final String position;
    private final String chessPiece;
    private final String color;

    public ChessPiece(final int id, final int roomId, final String position, final String chessPiece,
                      final String color) {
        this.id = id;
        this.roomId = roomId;
        this.position = position;
        this.chessPiece = chessPiece;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPosition() {
        return position;
    }

    public String getChessPiece() {
        return chessPiece;
    }

    public String getColor() {
        return color;
    }
}
