package chess.exception;

public class DeserializeException extends RuntimeException {

    private static final String MESSAGE = "[Error] 역직렬화 오류 발생";

    public DeserializeException(Throwable e) {
        super(MESSAGE, e);
    }

}
