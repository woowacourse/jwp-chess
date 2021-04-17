package chess.database.dao;

import chess.dto.RoomDTO;
import chess.dto.SaveRoomDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SpringRoomDAO {
    private JdbcTemplate jdbcTemplate;

    public SpringRoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addRoom(SaveRoomDTO saveRoomDTO) throws SQLException {
        String query = "INSERT INTO chess_room (room_name, turn, board) VALUES (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, saveRoomDTO.getRoomName());
            ps.setString(2, saveRoomDTO.getTurn());
            ps.setString(3, saveRoomDTO.getChessBoard());
            return ps;
        });
    }

    public int getLastInsertId() {
        String query = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public void updateRoom(RoomDTO roomDTO) {
        String query = "UPDATE chess_room SET turn = ?, board = ? where room_no = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, roomDTO.getTurn());
            ps.setString(2, roomDTO.getBoard());
            ps.setInt(3, roomDTO.getRoomNo());
            return ps;
        });
    }

    public RoomDTO findRoomByRoomNo(int roomNo) throws SQLException {
        String query = "SELECT * FROM chess_room WHERE room_no = ?";
        return jdbcTemplate.queryForObject(
                query,
                (resultSet, rowNum) -> {
                    RoomDTO roomDTO = new RoomDTO(
                            resultSet.getInt("room_no"),
                            resultSet.getString("room_name"),
                            resultSet.getString("turn"),
                            resultSet.getString("board"));
                    return roomDTO;
                },
                roomNo);
    }

    public List<RoomDTO> getAllRoom() throws SQLException {
        String query = "SELECT * FROM chess_room";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> {
                    RoomDTO roomDTO = new RoomDTO(
                            resultSet.getInt("room_no"),
                            resultSet.getString("room_name"),
                            resultSet.getString("turn"),
                            resultSet.getString("board"));
                    return roomDTO;
                });
    }

    public void deleteRoomByRoomNo(int roomNo) {
        String query = "DELETE FROM chess_room WHERE room_no = ?";
        jdbcTemplate.update(query, roomNo);
    }
}
