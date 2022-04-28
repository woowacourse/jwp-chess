package chess.dto;

public class MoveRequest {
    private String piece;
    private String from;
    private String to;
    private int gameId;

    public MoveRequest(String piece, String from, String to, int gameId) {
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

    public int getGameId() {
        return gameId;
    }
}
