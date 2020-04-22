package wooteco.chess.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
    public static Connection getConnection() {
        Connection conn = null;
        String server = "localhost:3306"; // MySQL 서버 주소
        String database = "chess_game"; // MySQL DATABASE 이름
        String option = "?useSSL=false&serverTimezone=UTC";
        String userName = "chess_manager"; //  MySQL 서버 아이디
        String password = "manager"; // MySQL 서버 비밀번호

        // 드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! JDBC Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }

        // 드라이버 연결
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + option, userName, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (SQLException e) {
            System.err.println("연결 오류:" + e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }
}
