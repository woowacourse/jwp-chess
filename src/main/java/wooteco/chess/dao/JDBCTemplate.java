package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JDBCTemplate {
	public void execute(String sql) {
		try (Connection connection = new SQLConnector().getConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			setParameters(statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public abstract void setParameters(PreparedStatement statement) throws SQLException;
}
