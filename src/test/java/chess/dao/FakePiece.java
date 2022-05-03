package chess.dao;

public class FakePiece {

    private final Long boardId;
    private final String position;
    private final String type;
    private final String color;

    public FakePiece(Long boardId, String position, String type, String color) {
        this.boardId = boardId;
        this.position = position;
        this.type = type;
        this.color = color;
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
