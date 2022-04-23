package chess.exception;

import org.springframework.http.HttpStatus;

public class DbConnectionException extends ApplicationException {

    public DbConnectionException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "DB 연결이 실패했습니다.");
    }
}
