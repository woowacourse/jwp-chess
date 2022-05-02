package chess.dto;

public class MoveRequest {

    private String piece;
    private String from;
    private String to;
    private long gameId;

    public MoveRequest(String piece, String from, String to, long gameId) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.gameId = gameId;
    }

    public MoveRequest() {
    }

    public String getPiece() {
        return piece;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public long getGameId() {
        return gameId;
    }
}
