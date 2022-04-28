package chess.dto;

public class MoveRequest {
    private int chessGameId;
    private String from;
    private String to;

    public MoveRequest(int chessGameId, String from, String to) {
        this.chessGameId = chessGameId;
        this.from = from;
        this.to = to;
    }

    public int getChessGameId() {
        return chessGameId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
