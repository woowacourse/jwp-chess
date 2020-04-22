package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface MySqlJdbcTemplateDao extends JdbcTemplateDao {
    String SERVER = "localhost:13306";
    String DATABASE = "chess";
    String OPTION = "?useSSL=false&serverTimezone=UTC";
    String USERNAME = "root";
    String PASSWORD = "root";

    default Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("!! JDBC Driver load 오류: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("연결 오류: " + e.getMessage());
            throw e;
        }
        System.err.println("알 수 없는 오류가 발생했습니다.");
        throw new RuntimeException();
    }
}
