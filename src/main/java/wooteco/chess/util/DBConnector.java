package wooteco.chess.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class DBConnector {

    private Connection connection;

    public DBConnector() {
        this.connection = connect();
    }

    private Connection connect() {
        Connection con = null;
        String server = "127.0.0.1:3306"; // MySQL 서버 주소
        String database = "wootechochess"; // MySQL DATABASE 이름
        String option = "?useSSL=false&serverTimezone=UTC";
        String userName = "root"; //  MySQL 서버 아이디
        String password = ""; // MySQL 서버 비밀번호

        // 드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! JDBC Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }
        // 드라이버 연결
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + option, userName, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (SQLException e) {
            System.err.println("연결 오류:" + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

    // 드라이버 연결해제
    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println("con 오류:" + e.getMessage());
        }
    }

    public ResultSet executeQuery(String query, String... args) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(query);

        for (int i = 1; i <= args.length; i++) {
            pstmt.setString(i, args[i - 1]);
        }
        return pstmt.executeQuery();
    }

    public int executeUpdate(String query, String... args) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 1; i <= args.length; i++) {
                pstmt.setString(i, args[i - 1]);
            }
            return pstmt.executeUpdate();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}