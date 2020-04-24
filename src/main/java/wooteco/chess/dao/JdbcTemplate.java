package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("CustomJdbcTemplate")
public class JdbcTemplate {
	private final DataSource dataSource;

	public JdbcTemplate(@Qualifier("CustomDataSource") DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public <T> T executeUpdate(String query, PreparedStatementSetter setter, RowMapper<T> mapper) throws SQLException {
		ResultSet resultSet = null;
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query,
					 Statement.RETURN_GENERATED_KEYS)) {
			setter.setValues(preparedStatement);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			return mapper.mapRow(resultSet);
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	public void executeUpdate(String query, PreparedStatementSetter setter) throws SQLException {
		executeUpdate(query, setter, resultSet -> null);
	}

	public <T> T executeQuery(String query, PreparedStatementSetter setter, RowMapper<T> mapper) throws SQLException {
		ResultSet resultSet = null;
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			setter.setValues(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			return mapper.mapRow(resultSet);
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	public <T> T executeQuery(String query, RowMapper<T> mapper) throws SQLException {
		return executeQuery(query, (preparedStatement) -> {
		}, mapper);
	}
}
