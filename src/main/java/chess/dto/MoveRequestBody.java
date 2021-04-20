package chess.dto;

public class MoveRequestBody {
    private final int gameId;
    private final String from;
    private final String to;

    public MoveRequestBody(int gameId, String from, String to) {
        this.gameId = gameId;
        this.from = from;
        this.to = to;
    }

    public int getGameId() {
        return gameId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
