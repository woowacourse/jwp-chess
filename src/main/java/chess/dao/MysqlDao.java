package chess.dao;

import static chess.constant.MysqlDbConstant.DRIVER_PATH;
import static chess.constant.MysqlDbConstant.PASSWORD;
import static chess.constant.MysqlDbConstant.URL;
import static chess.constant.MysqlDbConstant.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDao {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        loadDriver();
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void loadDriver() {
        try {
            Class.forName(DRIVER_PATH);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
