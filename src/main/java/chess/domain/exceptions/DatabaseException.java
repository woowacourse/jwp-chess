package chess.domain.exceptions;

import org.springframework.dao.DataAccessException;

public class DatabaseException extends DataAccessException {

    public DatabaseException() {
        super("데이터베이스에 문제가 발생했습니다.");
    }
}
