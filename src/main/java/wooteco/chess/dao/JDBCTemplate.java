package wooteco.chess.dao;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class JDBCTemplate {

    private DataSource dataSource;

    public JDBCTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() {
        Connection con = null;
        try {
            con = dataSource.getConnection();
        } catch (SQLException se) {
            System.err.println("연결 오류:" + se.getMessage());
        }

        return con;
    }

    public <T> T executeQuery(String query, PreparedStatementSetter setter, ResultSetMapper<T> mapper) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setter.setPreparedStatement(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            return mapper.map(resultSet);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    public <T> T executeQuery(String query, ResultSetMapper<T> mapper) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {
            return mapper.map(resultSet);
        }

    }

    public <T> T executeUpdate(String query, PreparedStatementSetter setter, ResultSetMapper<T> mapper) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setter.setPreparedStatement(preparedStatement);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            return mapper.map(resultSet);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    public void executeUpdate(String query, PreparedStatementSetter setter) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setter.setPreparedStatement(preparedStatement);
            preparedStatement.executeUpdate();
        }
    }
}
