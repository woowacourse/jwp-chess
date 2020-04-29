package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import wooteco.chess.jdbc.DataAccessException;

public class Connector {
	private static final String ADDRESS = "localhost:3306";
	private static final String NAME = "chess";
	private static final String OPTION = "?useSSL=false&serverTimezone=UTC";
	private static final String CONNECTION_FORMAT = String.format("jdbc:mysql://%s/%s%s", ADDRESS, NAME, OPTION);
	private static final String ID = "woowa2";
	private static final String PASSWORD = "test123";
	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

	public static Connection getConnection() {
		try {
			Class.forName(DRIVER_NAME);
			return DriverManager.getConnection(CONNECTION_FORMAT, ID, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e);
		}
	}

	public static void executeUpdate(String query, Object... args) {
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			for (int i = 1; i <= args.length; i++) {
				pstmt.setObject(i, args[i - 1]);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static <T> T executeQuery(String query, RowMapper<T> rowMapper) {
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			return rowMapper.mapRow(pstmt.executeQuery());
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
