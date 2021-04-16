package chess.dao;

import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection();

    boolean releaseConnection(Connection connection);
}
