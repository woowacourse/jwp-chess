package chess.database.room;

import chess.util.JsonConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static chess.database.DatabaseConnection.closeConnection;
import static chess.database.DatabaseConnection.getConnection;

public class RoomDAO {
    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO room (room_id, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, room.getName());
        pstmt.setString(2, room.getTurn());
        pstmt.setString(3, room.getState().toString());
        pstmt.executeUpdate();
        closeConnection(con);
    }

    public Room findByRoomId(String name) throws SQLException {
        String query = "SELECT * FROM room WHERE name = ?";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();

        Room room = Optional.ofNullable(getRoom(rs))
                .orElseThrow(IllegalArgumentException::new);
        closeConnection(con);
        return room;
    }

    private Room getRoom(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        return new Room(
                rs.getString("name"),
                rs.getString("turn"),
                JsonConverter.toJsonObject(rs.getString("state")));
    }

    public void validateRoomExistence(String name) throws SQLException {
        String query = "SELECT * FROM room WHERE name = ?";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            throw new IllegalArgumentException();
        }
        closeConnection(con);
    }

    public List<Room> getAllRoom() throws SQLException {
        String query = "SELECT room_id FROM room";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<Room> rooms = new ArrayList<>();
        while (rs.next()) {
            rooms.add(new Room(rs.getString("name"), null, null));
        }
        closeConnection(con);
        return rooms;
    }
}
