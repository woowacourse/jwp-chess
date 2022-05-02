package chess.dao.entity;

public class PieceEntity {

    private Long id;
    private final String position;
    private final String type;
    private final String color;
    private final Long gameId;

    public PieceEntity(Long id, String position, String type, String color, Long gameId) {
        this.id = id;
        this.position = position;
        this.type = type;
        this.color = color;
        this.gameId = gameId;
    }

    public PieceEntity(String position, String type, String color, Long gameId) {
        this.position = position;
        this.type = type;
        this.color = color;
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
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

    public Long getGameId() {
        return gameId;
    }
}
