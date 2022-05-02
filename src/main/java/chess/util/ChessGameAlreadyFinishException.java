package chess.util;

public class ChessGameAlreadyFinishException extends RuntimeException{

    public ChessGameAlreadyFinishException(String message) {
        super(message);
    }
}
