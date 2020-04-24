package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component("CustomDataSource")
public class DataSource {
	private final JdbcConfiguration jdbcConfiguration;

	public DataSource(JdbcConfiguration jdbcConfiguration) {
		this.jdbcConfiguration = jdbcConfiguration;
	}

	public Connection getConnection() throws SQLException {
		System.err.println(jdbcConfiguration);
		try {
			Class.forName(jdbcConfiguration.getDriverClassName());
			System.err.println(jdbcConfiguration);
			return DriverManager.getConnection(jdbcConfiguration.getUrl(), jdbcConfiguration.getUsername(),
					jdbcConfiguration.getPassword());
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver load 오류: " + e.getMessage());
		}
		throw new SQLException("연결이 설정되지 않았습니다.");
	}
}
