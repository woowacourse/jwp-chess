package chess.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChessLogDao {
    private static final String MOVE = "move ";
    private static final String DELIMITER = " ";

    private DBConnection dbConnection;

    public ChessLogDao(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public java.sql.Connection getConnection() {
        java.sql.Connection con = null;

        loading();
        return dbConnection.connection(con);
    }

    private void loading() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! JDBC Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeConnection(java.sql.Connection con) {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            Logger.getLogger("con 오류:" + e.getMessage());
        }
    }

    public void addLog(String roomId, String target, String destination) {
        java.sql.Connection connection = getConnection();
        String query = "INSERT INTO chessgame (room_id, target, destination) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setString(1, roomId);
            pstmt.setString(2, target);
            pstmt.setString(3, destination);
            pstmt.executeUpdate();
            closeConnection(connection);
        }
        catch (SQLException e) {
            Logger.getLogger("로그 추가 오류:" + e.getMessage());
            e.printStackTrace();
            closeConnection(connection);
        }
    }

    public List<String> applyCommand(String userId) {
        java.sql.Connection connection = getConnection();
        String query = "SELECT target, destination FROM chess.chessgame WHERE room_id = ? ORDER BY command_date ASC;";
        try (PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            List<String> commands = new ArrayList<>();

            while (rs.next()) {
                commands.add(MOVE + rs.getString(1) + DELIMITER + rs.getString(2));
            }
            closeConnection(connection);

            return commands;
        }
        catch (SQLException e) {
            Logger.getLogger("커맨드 적용 오류:" + e.getMessage());
            e.printStackTrace();
            closeConnection(connection);
            return null;
        }
    }

    public void deleteLog(String roomId) {
        java.sql.Connection connection = getConnection();
        String query = "DELETE FROM chess.chessgame WHERE room_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setString(1, roomId);
            pstmt.executeUpdate();
            closeConnection(connection);
        }
        catch (SQLException e) {
            Logger.getLogger("로그 제거 오류:" + e.getMessage());
            e.printStackTrace();
            closeConnection(connection);
        }
    }
}
