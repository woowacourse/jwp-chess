package chess.dao;

import java.util.function.Supplier;
import org.springframework.dao.DataAccessException;

public class Query<T> {

    private final Supplier<T> query;

    public Query(Supplier<T> query) {
        this.query = query;
    }

    public T executeOrThrow(String exceptionMessage) {
        try {
            return query.get();
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public static int executeAndGetInt(Supplier<Integer> query) {
        Integer value = query.get();
        if (value == null) {
            throw new IllegalArgumentException("데이터 조회에 실패하였습니다.");
        }
        return value;
    }
}
