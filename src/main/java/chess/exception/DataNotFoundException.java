package chess.exception;

import java.util.NoSuchElementException;

public class DataNotFoundException extends NoSuchElementException {

    public DataNotFoundException(final Class<?> dataType) {
        super(String.format("%s 타입의 데이터를 조회하는데 실패했습니다.", dataType.getName()));
    }

}
