package chess.dao;

import java.util.function.Supplier;
import org.springframework.dao.DataAccessException;

public class StatementExecutor<T> {

    private final Supplier<T> statement;

    public StatementExecutor(Supplier<T> statement) {
        this.statement = statement;
    }

    public T execute() {
        try {
            return statement.get();
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("데이터베이스 접속 과정에 문제가 발생하였습니다.");
        }
    }

    public T executeOrThrow(Supplier<RuntimeException> exceptionSupplier) {
        try {
            return statement.get();
        } catch (DataAccessException e) {
            throw exceptionSupplier.get();
        }
    }

    public boolean countAndCheckExistence() {
        int foundRowCount = (int) execute();
        return foundRowCount > 0;
    }

    public void updateAndThrowOnNonEffected(Supplier<RuntimeException> exceptionSupplier) {
        int effectedRowCount = (int) execute();
        if (effectedRowCount == 0) {
            throw exceptionSupplier.get();
        }
    }
}