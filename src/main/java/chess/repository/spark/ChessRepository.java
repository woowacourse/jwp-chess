package chess.repository.spark;

import chess.domain.history.History;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChessRepository {

    public List<History> findAllHistories() throws SQLException {
        String query = "SELECT * FROM HISTORY";
        try (Connection connection = ConnectionManager.makeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<History> histories = new ArrayList<>();
            while (resultSet.next()) {
                History history = generateHistoryFrom(resultSet);
                histories.add(history);
            }
            return histories;
        }
    }

    private History generateHistoryFrom(ResultSet resultSet) throws SQLException {
        return new History(resultSet.getString("source"),
                resultSet.getString("destination"),
                resultSet.getString("team_type"));
    }

    public void insertHistory(String source, String destination, String teamType) throws SQLException {
        String query = "INSERT INTO HISTORY (SOURCE, DESTINATION, TEAM_TYPE) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionManager.makeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, source);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, teamType);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAllHistories() throws SQLException {
        String query = "TRUNCATE HISTORY";
        try (Connection connection = ConnectionManager.makeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }
}
