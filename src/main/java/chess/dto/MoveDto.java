package chess.dto;

public final class MoveDto {

    private int gameId;
    private String from;
    private String to;

    public MoveDto(final String gameId, final String from, final String to) {
        this.gameId = Integer.parseInt(gameId);
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
}
