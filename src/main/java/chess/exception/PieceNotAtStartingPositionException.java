package chess.exception;

public class PieceNotAtStartingPositionException extends ClientException {

    private static final String MESSAGE = "[ERROR] 출발 위치에는 말이 있어야 합니다.";

    public PieceNotAtStartingPositionException() {
        super(MESSAGE);
    }
}
