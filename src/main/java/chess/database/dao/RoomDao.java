package chess.database.dao;

import chess.dto.SparkRoomDto;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static chess.controller.web.SparkChessController.gson;
import static chess.database.DatabaseConnection.closeConnection;
import static chess.database.DatabaseConnection.getConnection;

public class RoomDao {
    public void addRoom(SparkRoomDto sparkRoomDto) throws SQLException {
        String query = "INSERT INTO room (room_id, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, sparkRoomDto.getName());
        pstmt.setString(2, sparkRoomDto.getTurn());
        pstmt.setString(3, sparkRoomDto.getState().toString());
        pstmt.executeUpdate();
        closeConnection(con);
    }

    public SparkRoomDto findByRoomId(String roomId) throws SQLException {
        String query = "SELECT * FROM room WHERE room_id = ?";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, roomId);
        ResultSet rs = pstmt.executeQuery();

        SparkRoomDto sparkRoomDto = (SparkRoomDto) Optional.ofNullable(getRoom(rs))
                .orElseThrow(IllegalArgumentException::new);
        closeConnection(con);
        return sparkRoomDto;
    }

    private SparkRoomDto getRoom(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        return new SparkRoomDto(
                rs.getString("room_id"),
                rs.getString("turn"),
                gson.fromJson(rs.getString("state"), JsonObject.class));
    }

    public void validateRoomExistence(String roomId) throws SQLException {
        String query = "SELECT * FROM room WHERE room_id = ?";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, roomId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            throw new IllegalArgumentException();
        }
        closeConnection(con);
    }

    public List<SparkRoomDto> getAllRoom() throws SQLException {
        String query = "SELECT room_id FROM room";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<SparkRoomDto> sparkRoomDtos = new ArrayList<>();
        while (rs.next()) {
            sparkRoomDtos.add(new SparkRoomDto(rs.getString("room_id"), null, null));
        }
        closeConnection(con);
        return sparkRoomDtos;
    }
}
