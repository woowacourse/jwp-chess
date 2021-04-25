package chess.dto;

public class MoveRequest {
    private final long gameId;
    private final String from;
    private final String to;

    public MoveRequest(long gameId, String from, String to) {
        this.gameId = gameId;
        this.from = from;
        this.to = to;
    }

    public long getGameId() {
        return gameId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
