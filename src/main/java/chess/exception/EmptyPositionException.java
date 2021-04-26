package chess.exception;

public class EmptyPositionException extends BlankException {

    private static final String MESSAGE = "비어 있는 칸입니다.";

    public EmptyPositionException() {
        super(MESSAGE);
    }
}
