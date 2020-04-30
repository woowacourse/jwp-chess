package chess.dao;

import chess.dao.exceptions.DaoNoneSelectedException;
import chess.entity.RoomEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoomDao {
	private final ConnectionDao connectionDao;

	public RoomDao(final ConnectionDao connectionDao) {
		this.connectionDao = connectionDao;
	}

	public int addRoomByRoomName(final String roomName) throws SQLException {
		final String query = "INSERT INTO room (room_name) VALUES (?)";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, roomName);
			return preparedStatement.executeUpdate();
		}
	}

	public RoomEntity findRoomByRoomName(final String roomName) throws SQLException {
		final String query = "SELECT * FROM room WHERE room_name = ?";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, roomName);
			return prepareRoomDto(preparedStatement);
		}
	}

	private RoomEntity prepareRoomDto(final PreparedStatement preparedStatement) throws SQLException {
		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			validateHasResult(resultSet);
			return new RoomEntity(resultSet.getString("room_name"));
		}
	}

	private void validateHasResult(final ResultSet resultSet) throws SQLException {
		if (!resultSet.next()) {
			throw new DaoNoneSelectedException();
		}
	}

	public List<RoomEntity> findAllRooms() throws SQLException {
		final String query = "SELECT * FROM room";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			return prepareRoomDtos(preparedStatement);
		}
	}

	private List<RoomEntity> prepareRoomDtos(final PreparedStatement preparedStatement) throws SQLException {
		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			return collectRoomDtos(resultSet);
		}
	}

	private List<RoomEntity> collectRoomDtos(final ResultSet resultSet) throws SQLException {
		final List<RoomEntity> roomEntities = new ArrayList<>();
		while (resultSet.next()) {
			roomEntities.add(new RoomEntity(resultSet.getString("room_name")));
		}
		return roomEntities;
	}

	public int deleteRoomByRoomName(final String roomName) throws SQLException {
		final String query = "DELETE FROM room WHERE room_name = ?";

		try (final Connection connection = connectionDao.getConnection();
			 final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, roomName);
			return preparedStatement.executeUpdate();
		}
	}
}
