package chess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException  {

    private static final String MESSAGE = "인증되지않은 유저입니다";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
