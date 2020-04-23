package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface JdbcTemplateDao {
    Connection getConnection() throws SQLException;
}
