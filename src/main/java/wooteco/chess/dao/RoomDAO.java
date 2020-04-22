package wooteco.chess.dao;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.room.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private static final RoomDAO ROOM_DAO = new RoomDAO();

    public static RoomDAO getInstance() {
        return ROOM_DAO;
    }

    public void addRoom(String roomName, String roomColor) throws SQLException {
        String query = "INSERT INTO room(room_name, room_color) VALUES (?, ?)";

        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, roomName);
            pstmt.setString(2, roomColor);
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void removeRoomById(int roomId) throws SQLException {
        String query = "DELETE FROM room WHERE room_id = ?";

        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, roomId);

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void updateRoomColorById(int roomId, Color roomColor) throws SQLException {
        String query = "UPDATE room SET room_color = ? WHERE room_id = ?";

        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, roomColor.name());
            pstmt.setInt(2, roomId);
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.executeUpdate(query, pss);
    }

    public Room findRoomById(int roomId) throws SQLException {
        String query = "SELECT room_id, room_name, room_color FROM room WHERE room_id = ?";

        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, roomId);

        RowMapper rm = rs -> {
            if (!rs.next()) {
                return null;
            }
            return new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_name"),
                    rs.getString("room_color")
            );
        };
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return (Room) jdbcTemplate.executeQuery(query, pss, rm);
    }

    public int findRoomIdByRoomName(String roomName) throws SQLException {
        String query = "SELECT room_id FROM room WHERE room_name = ?";

        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, roomName);

        RowMapper rm = rs -> {
            if (!rs.next()) {
                return 0;
            }

            return rs.getInt("room_id");
        };
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return (int) jdbcTemplate.executeQuery(query, pss, rm);
    }

    public List<Room> findAllRoom() throws SQLException {
        String query = "SELECT room_id, room_name, room_color FROM room";

        PreparedStatementSetter pss = pstmt -> {
        };

        RowMapper rm = rs -> {
            List<Room> rooms = new ArrayList<>();

            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                String roomName = rs.getString("room_name");
                String roomColor = rs.getString("room_color");

                rooms.add(new Room(roomId, roomName, roomColor));
            }
            return rooms;
        };
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return (List<Room>) jdbcTemplate.executeQuery(query, pss, rm);
    }

    public Color findRoomColorById(int roomId) throws SQLException {
        String query = "SELECT room_color FROM room WHERE room_id = ?";

        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, roomId);

        RowMapper rm = rs -> {
            if (!rs.next()) {
                return null;
            }
            return Color.valueOf(rs.getString("room_color"));
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return (Color) jdbcTemplate.executeQuery(query, pss, rm);
    }
}
