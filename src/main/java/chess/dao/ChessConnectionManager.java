package chess.dao;

import java.sql.*;

public class ChessConnectionManager implements ConnectionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/chess";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    @Override
    public <T> T executeQuery(ConnectionMapper<T> connectionMapper, String sql) {
        try (final Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return connectionMapper.execute(preparedStatement);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Connection getConnection() {
        loadDriver();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
