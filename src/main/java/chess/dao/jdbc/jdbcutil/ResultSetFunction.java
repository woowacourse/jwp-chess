package chess.dao.jdbc.jdbcutil;

import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetFunction<ResultSet, T> {
    T convert(ResultSet resultSet) throws SQLException;
}
