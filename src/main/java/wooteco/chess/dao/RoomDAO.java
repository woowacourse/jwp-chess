package wooteco.chess.dao;

import wooteco.chess.connection.Connector;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.room.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection connection;

    public RoomDAO() throws SQLException {
        this.connection = Connector.getConnection();
    }

    public List<Room> findAllRoom() throws SQLException {
        String query = "SELECT * FROM room";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(new Room(resultSet.getString("title")));
            }
            return rooms;
        }
    }

    public void insertRoom(final Long id, final String title, final String turn) throws SQLException {
        String query = "INSERT INTO room (id, title, turn) VALUES (?, ?, ?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, turn);
            preparedStatement.executeUpdate();
        }
    }

    public void updateTurn(final Long roomId, Team targetTeam) throws SQLException {
        String query = "UPDATE room set turn = (?) WHERE id = (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, targetTeam.name());
            preparedStatement.setLong(2, roomId);
            preparedStatement.executeUpdate();
        }
    }

    public String findTitleById(final Long roomId) throws SQLException {
        String query = "SELECT title FROM room WHERE id = (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            String title = "";
            while(resultSet.next()) {
                return resultSet.getString("title");
            }
            return title;
        }
    }

    public Team findTurnById(final Long roomId) throws SQLException {
        String query = "SELECT turn FROM room WHERE id = (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();

            Team output = Team.BLANK;
            while (resultSet.next()) {
                output = Team.of(resultSet.getString("turn"));
            }
            return output;
        }
    }

    public void deleteRoomById(Long roomId) throws SQLException {
        String query = "DELETE FROM room WHERE id = (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, roomId);
            preparedStatement.executeUpdate();
        }
    }

    public Long findCurrentMaxId() throws SQLException {
        String query = "SELECT MAX(id) FROM room";
        Long id = (long)0;
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                return resultSet.getLong(1);
            }
        }
        return id;
    }
}
