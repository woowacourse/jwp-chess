package web.exception;

public class ChessGameException extends RuntimeException {

    private final int chessGameId;

    public ChessGameException(int chessGameId, String message) {
        super(message);
        this.chessGameId = chessGameId;
    }

    public int getChessGameId() {
        return chessGameId;
    }
}
