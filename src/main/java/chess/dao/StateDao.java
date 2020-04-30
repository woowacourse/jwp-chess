package chess.dao;

import chess.dao.exceptions.DaoNoneSelectedException;
import chess.entity.StateEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StateDao {
	private final ConnectionDao connectionDao;

	public StateDao(final ConnectionDao connectionDao) {
		this.connectionDao = connectionDao;
	}

	public int addState(final String state, final int roomId) throws SQLException {
		final String query = "INSERT INTO state (state, room_id) "
				+ "VALUES (?, ?)";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, state);
			preparedStatement.setInt(2, roomId);
			return preparedStatement.executeUpdate();
		}
	}

	public StateEntity findStateByRoomId(final int roomId) throws SQLException {
		final String query = "SELECT * FROM state WHERE room_id=?";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, roomId);
			return prepareStateDto(preparedStatement);
		}
	}

	private StateEntity prepareStateDto(final PreparedStatement preparedStatement) throws SQLException {
		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			validateHasResult(resultSet);

			return new StateEntity(resultSet.getString("state"), resultSet.getInt("room_id"));
		}
	}

	private void validateHasResult(final ResultSet resultSet) throws SQLException {
		if (!resultSet.next()) {
			throw new DaoNoneSelectedException();
		}
	}

	public int setStateByRoomId(final int roomId, final String state) throws SQLException {
		final String query = "UPDATE state SET state=? WHERE room_id=?";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, state);
			preparedStatement.setInt(2, roomId);
			return preparedStatement.executeUpdate();
		}
	}
}
