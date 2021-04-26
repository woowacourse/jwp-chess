package chess.dao.dto;

public class PieceDto {

    private final long id;
    private final long gameId;
    private final int x;
    private final int y;
    private final String color;
    private final char shape;

    public PieceDto(final long id, final long gameId, final int x, final int y, final String color,
        final char shape) {
        this.id = id;
        this.gameId = gameId;
        this.x = x;
        this.y = y;
        this.color = color;
        this.shape = shape;
    }

    public long getId() {
        return id;
    }

    public long getGameId() {
        return gameId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public char getShape() {
        return shape;
    }

}
