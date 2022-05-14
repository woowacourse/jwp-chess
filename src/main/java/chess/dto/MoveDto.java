package chess.dto;

public final class MoveDto {

    private int gameId;
    private String from;
    private String to;

    public MoveDto(final int gameId, final String from, final String to) {
        this.gameId = gameId;
        this.from = from;
        this.to = to;
    }

    public MoveDto() {
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "MoveDto{" +
                "gameId=" + gameId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
