package chess.exception;

import org.springframework.dao.DataAccessException;

public class DataBaseException extends RuntimeException {

    private static final String MESSAGE = "[Error] 데이터베이스 오류";

    public DataBaseException(DataAccessException e) {
        super(MESSAGE, e);
    }

}
