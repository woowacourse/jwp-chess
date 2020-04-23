package wooteco.chess.domain.dao;

import wooteco.chess.domain.connection.Connector;
import wooteco.chess.domain.piece.Team;

import java.sql.*;

public class TurnDAO {
    private Connection connection;

    public TurnDAO() throws SQLException {
        this.connection = Connector.getConnection();
    }

    public void insertTurn(Team targetTeam) throws SQLException {
        String query = "INSERT INTO turn (team) VALUES (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, targetTeam.name());
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
}
