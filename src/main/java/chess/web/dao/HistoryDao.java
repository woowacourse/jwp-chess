package chess.web.dao;

import chess.web.db.DriveManager;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HistoryDao {

    public Optional<String> insert(String name) throws SQLException {
        final String query = "INSERT INTO History (Name) VALUES (?)";
        Optional<String> id = Optional.empty();
        try (Connection connection = DriveManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            id = receiveRawId(preparedStatement);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return id;
    }

    private Optional<String> receiveRawId(PreparedStatement preparedStatement)
        throws SQLException {
        Optional<String> id;
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            resultSet.next();
            id = Optional.ofNullable(resultSet.getString(1));
        }
        return id;
    }

    public Optional<Integer> findIdByName(String name) throws SQLException {
        String query = "SELECT * FROM History WHERE Name = ? AND is_end = false";
        Optional<Integer> id = Optional.empty();
        try (Connection connection = DriveManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            findRawIdNumber(preparedStatement);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return id;
    }

    private Optional<Integer> findRawIdNumber(PreparedStatement preparedStatement)
        throws SQLException {
        Optional<Integer> id = Optional.empty();
        try (ResultSet rs = preparedStatement.executeQuery()) {
            if (!rs.next()) return id;
            id = Optional.of(rs.getInt("history_id"));
        }
        return id;
    }

    public void delete(String name) throws SQLException {
        String query = "DELETE FROM History WHERE Name = ?";
        try (Connection connection = DriveManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<String> selectActive() throws SQLException {
        List<String> names = new ArrayList<>();
        String query = "SELECT * FROM History WHERE is_end = false";
        try (Connection connection = DriveManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            insertActiveHistoryName(names, rs);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return names;
    }

    private void insertActiveHistoryName(List<String> names, ResultSet rs) throws SQLException {
        while (rs.next())
            names.add(rs.getString("name"));
    }

    public void updateEndState(String historyId) throws SQLException {
        String query = "UPDATE History SET is_end = 1 WHERE history_id = ?";
        try (Connection connection = DriveManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, historyId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
