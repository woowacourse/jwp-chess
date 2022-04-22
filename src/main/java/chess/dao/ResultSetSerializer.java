package chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetSerializer<T> {
    T serialize(final ResultSet resultSet) throws SQLException;
}
