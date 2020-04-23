package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDataSource implements DataSource {
	private final JdbcConfiguration jdbcConfiguration;

	public AbstractDataSource(JdbcConfiguration jdbcConfiguration) {
		this.jdbcConfiguration = jdbcConfiguration;
	}

	public Connection getConnection() throws SQLException {
		try {
			Class.forName(jdbcConfiguration.getDatabaseDriver());
			return DriverManager.getConnection(jdbcConfiguration.getUrl(), jdbcConfiguration.getUserName(),
					jdbcConfiguration.getPassword());
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver load 오류: " + e.getMessage());
		}
		throw new SQLException("연결이 설정되지 않았습니다.");
	}
}
