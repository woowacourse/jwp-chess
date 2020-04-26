package wooteco.chess.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component("CustomDataSource")
public class MySqlDataSource implements DataSource {

	private static final String server = "127.0.0.1:13306"; // MySQL 서버 주소
	private static final String database = "woowa_level_01_chess"; // MySQL DATABASE 이름
	private static final String option = "?useSSL=false&serverTimezone=UTC";
	private static final String userName = "root"; //  MySQL 서버 아이디
	private static final String password = "root"; // MySQL 서버 비밀번호
	private static final String CONNECTION_FORMAT = "jdbc:mysql://%s/%s%s";

	private MySqlDataSource() {
	}

	public static MySqlDataSource getInstance() {
		return LazyHolder.INSTANCE;
	}

	@Override
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(String.format(CONNECTION_FORMAT, server, database, option), userName,
				password);
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver load 오류: " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("연결 오류:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static class LazyHolder {
		private static final MySqlDataSource INSTANCE = new MySqlDataSource();
	}

}
