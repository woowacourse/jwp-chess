package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	public static Connection getMysqlConnection() {
		Connection con = null;
		String server = "localhost:13306";
		String database = "chessgame";
		String option = "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
		String userName = "root";
		String password = "root";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + option, userName, password);
			System.out.println("정상적으로 연결되었습니다.");
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("JDBC 오류: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}

		return con;
	}
}
