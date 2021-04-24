package chess.dto;

public class MoveRequest {
    private int gameId;
    private String from;
    private String to;

    public MoveRequest(int gameId, String from, String to) {
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

    public void setFrom(String from) {
        this.from = from;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
