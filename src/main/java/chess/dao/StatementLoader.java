package chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementLoader {
    PreparedStatement create(final Connection connection) throws SQLException;
}
