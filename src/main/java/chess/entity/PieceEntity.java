package chess.entity;

public class PieceEntity {
    private final Long id;
    private final Long gameId;
    private final String position;
    private final String type;
    private final boolean white;

    public PieceEntity(Long id, Long gameId, String position, String type, boolean white) {
        this.id = id;
        this.gameId = gameId;
        this.position = position;
        this.type = type;
        this.white = white;
    }

    public Long getId() {
        return id;
    }

    public String getCamp() {
        if (white) {
            return "WHITE";
        }
        return "BLACK";
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPosition() {
        return position;
    }

    public boolean isWhite() {
        return white;
    }

    public String getType() {
        return type;
    }
}
