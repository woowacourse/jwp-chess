package chess.exception;

public class SerializeException extends RuntimeException {

    private static final String MESSAGE = "[Error] 직렬화 오류 발생";

    public SerializeException(Throwable e) {
        super(MESSAGE, e);
    }

}
