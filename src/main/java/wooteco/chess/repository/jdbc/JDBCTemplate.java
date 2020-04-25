package wooteco.chess.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import wooteco.chess.exception.SQLAccessException;
import wooteco.chess.repository.DataSource;
import wooteco.chess.repository.PrepareStatementSetter;
import wooteco.chess.repository.RowMapper;

@Component
public class JDBCTemplate {
	private final DataSource dataSource;

	public JDBCTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	<T> Optional<T> executeQuery(String query, RowMapper<T> mapper, PrepareStatementSetter setter) {
		try (Connection con = dataSource.getConnection();
			 PreparedStatement preparedStatement = con.prepareStatement(query)) {
			setter.set(preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return Optional.empty();
			}
			return Optional.of(mapper.map(resultSet));
		} catch (SQLException e) {
			throw new SQLAccessException();
		}
	}

	int executeUpdate(String query, PrepareStatementSetter setter) {
		try (Connection con = dataSource.getConnection();
			 PreparedStatement preparedStatement = con.prepareStatement(query)) {
			setter.set(preparedStatement);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLAccessException();
		}
	}
}
