package chess.exception;

public class NotMoveToTargetPosition extends RuntimeException{
    private static final String MESSAGE = "해당 위치로는 이동할 수 없습니다.";

    public NotMoveToTargetPosition() {
        super(MESSAGE);
    }
    
}
