package chess.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionMapper<T> {

    T execute(PreparedStatement preparedStatement) throws SQLException;
}
