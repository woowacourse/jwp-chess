package chess.dao;

import chess.entity.StatusRecordEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StatusRecordDao {
	private final ConnectionDao connectionDao;

	public StatusRecordDao(final ConnectionDao connectionDao) {
		this.connectionDao = connectionDao;
	}

	public List<StatusRecordEntity> findStatusRecords() throws SQLException {
		final String query = "SELECT * "
				+ "FROM status_record "
				+ "ORDER BY room_name";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			return prepareStatusRecordDtos(preparedStatement);
		}
	}

	private List<StatusRecordEntity> prepareStatusRecordDtos(final PreparedStatement preparedStatement)
			throws SQLException {
		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			return collectStatusRecordDtos(resultSet);
		}
	}

	private List<StatusRecordEntity> collectStatusRecordDtos(final ResultSet resultSet)
			throws SQLException {
		final List<StatusRecordEntity> statusRecordEntities = new ArrayList<>();
		while (resultSet.next()) {
			statusRecordEntities.add(
					new StatusRecordEntity(resultSet.getString("record"),
							resultSet.getDate("game_date"), resultSet.getString("room_name")));
		}
		return statusRecordEntities;
	}

	public int addStatusRecord(final String record, final int roomId) throws SQLException {
		final String query = "INSERT INTO status_record(record, room_name) "
				+ "VALUES(?, (SELECT room_name FROM room WHERE room.id = ?))";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, record);
			preparedStatement.setInt(2, roomId);
			return preparedStatement.executeUpdate();
		}
	}
}
