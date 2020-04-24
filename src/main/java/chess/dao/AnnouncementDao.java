package chess.dao;

import chess.dao.exceptions.DaoNoneSelectedException;
import chess.dto.AnnouncementDto;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AnnouncementDao {
	private final ConnectionDao connectionDao;

	public AnnouncementDao(final ConnectionDao connectionDao) {
		this.connectionDao = connectionDao;
	}

	public int addAnnouncement(final String message, final int roomId) throws SQLException {
		String query = "INSERT INTO announcement(message, room_id) VALUES(?, ?)";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, message);
			preparedStatement.setInt(2, roomId);
			return preparedStatement.executeUpdate();
		}
	}

	public AnnouncementDto findAnnouncementByRoomId(final int roomId) throws SQLException {
		final String query = "SELECT * FROM announcement WHERE room_id = ?";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, roomId);
			return prepareAnnouncementDto(preparedStatement);
		}
	}

	private AnnouncementDto prepareAnnouncementDto(final PreparedStatement preparedStatement) throws SQLException {
		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			validateHasResult(resultSet);
			return new AnnouncementDto(resultSet.getInt("id"),
					resultSet.getString("message"), resultSet.getInt("room_id"));
		}
	}

	private void validateHasResult(final ResultSet resultSet) throws SQLException {
		if (!resultSet.next()) {
			throw new DaoNoneSelectedException();
		}
	}

	public int setAnnouncementByRoomId(final int roomId, final String message) throws SQLException {
		final String query = "UPDATE announcement SET message=? WHERE room_id=?";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, message);
			preparedStatement.setInt(2, roomId);
			return preparedStatement.executeUpdate();
		}
	}
}
