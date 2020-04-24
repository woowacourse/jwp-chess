package chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static final String SERVER = "127.0.0.1:13306";
    public static final String DATABASE = "Chess";
    public static final String OPTION = "?useSSL=false&serverTimezone=UTC";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("※ JDBC Driver load 오류: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USER_NAME,
                                                     PASSWORD);
        } catch (SQLException e) {
            System.err.println("※ 연결 오류: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return connection;
    }
}
