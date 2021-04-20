package chess.exception;

public class ChessException extends IllegalArgumentException{
    private String errorMessage;
    private int gameId;

    public ChessException(String errorMessage, int gameId) {
        this.errorMessage = errorMessage;
        this.gameId = gameId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getGameId() {
        return gameId;
    }
}
