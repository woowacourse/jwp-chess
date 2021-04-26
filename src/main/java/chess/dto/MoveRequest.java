package chess.dto;

public class MoveRequest {
    private long gameId;
    private String from;
    private String to;

    public MoveRequest() {
    }

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

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
