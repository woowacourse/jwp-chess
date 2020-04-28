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
                rooms.add(new Room(resultSet.getLong("id"), resultSet.getString("title")));
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

    public void updateTurn(Team targetTeam) throws SQLException {
        String query = "UPDATE turn set team = (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, targetTeam.name());
            preparedStatement.executeUpdate();
        }
    }

    public Team findTurn() throws SQLException {
        String query = "SELECT * FROM turn";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            Team output = Team.BLANK;
            while (resultSet.next()) {
                output = Team.of(resultSet.getString("team"));
            }
            return output;
        }
    }

    public void deleteTurn() throws SQLException {
        String query = "TRUNCATE turn";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
