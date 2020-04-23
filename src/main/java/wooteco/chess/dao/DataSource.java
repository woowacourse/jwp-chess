package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource {
	Connection getConnection() throws SQLExc, SQLException;
}
