package wooteco.chess.domain.dao;

import wooteco.chess.domain.connection.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinishDAO {
    private Connection connection;

    public FinishDAO() throws SQLException {
        this.connection = Connector.getConnection();
    }

    public void insertFinish(final boolean isFinish) throws SQLException {
        String query = "INSERT INTO finish (isFinish) VALUES (?)";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, String.valueOf(isFinish));
            preparedStatement.executeUpdate();
        }
    }

    public void updateFinish(final boolean isFinish) throws SQLException {
        String query = "UPDATE finish set isFinish = (?)";

        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, String.valueOf(isFinish));
            preparedStatement.executeUpdate();
        }
    }

    public String getIsFinish() throws SQLException {
        String query = "SELECT * FROM finish";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            String output = "";
            while (resultSet.next()) {
                output = resultSet.getString("isFinish");
            }
            return output;
        }
    }

    public void deleteFinish() throws SQLException {
        String query = "TRUNCATE finish";
        try (final Connection connection = Connector.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }
}
