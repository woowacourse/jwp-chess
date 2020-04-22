package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDataSource implements DataSource {
	private final JdbcConfiguration jdbcConfiguration;

	public AbstractDataSource(JdbcConfiguration jdbcConfiguration) {
		this.jdbcConfiguration = jdbcConfiguration;
	}

	public Connection getConnection() {
		try {
			Class.forName(jdbcConfiguration.getDatabaseDriver());
			return DriverManager.getConnection(jdbcConfiguration.getUrl(), jdbcConfiguration.getUserName(),
					jdbcConfiguration.getPassword());
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver load 오류: " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("연결 오류:" + e.getMessage());
		}
		return null;
	}
}
