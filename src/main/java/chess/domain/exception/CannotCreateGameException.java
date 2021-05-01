package chess.domain.exception;

public class CannotCreateGameException extends RuntimeException{
    private static String MESSAGE = "방이 너무 많습니다. 게임을 지우고 생성해주세요";

    public CannotCreateGameException() {
        super(MESSAGE);
    }
}
