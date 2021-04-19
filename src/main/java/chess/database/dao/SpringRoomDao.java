package chess.database.dao;

import chess.dto.RoomDto;
import chess.dto.SaveRoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SpringRoomDao {
    private JdbcTemplate jdbcTemplate;

    public SpringRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addRoom(SaveRoomDto saveRoomDto) {
        String query = "INSERT INTO chess_room (room_name, turn, board) VALUES (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, saveRoomDto.getRoomName());
            ps.setString(2, saveRoomDto.getTurn());
            ps.setString(3, saveRoomDto.getChessBoard());
            return ps;
        });
    }

    public int getLastInsertId() {
        String query = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public void updateRoom(RoomDto roomDto) {
        String query = "UPDATE chess_room SET turn = ?, board = ? where room_no = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, roomDto.getTurn());
            ps.setString(2, roomDto.getBoard());
            ps.setInt(3, roomDto.getRoomNo());
            return ps;
        });
    }

    public RoomDto findRoomByRoomNo(int roomNo) {
        String query = "SELECT * FROM chess_room WHERE room_no = ?";
        return jdbcTemplate.queryForObject(
                query,
                (resultSet, rowNum) -> {
                    RoomDto roomDto = new RoomDto(
                            resultSet.getInt("room_no"),
                            resultSet.getString("room_name"),
                            resultSet.getString("turn"),
                            resultSet.getString("board"));
                    return roomDto;
                },
                roomNo);
    }

    public List<RoomDto> getAllRoom() {
        String query = "SELECT * FROM chess_room";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> {
                    RoomDto roomDto = new RoomDto(
                            resultSet.getInt("room_no"),
                            resultSet.getString("room_name"),
                            resultSet.getString("turn"),
                            resultSet.getString("board"));
                    return roomDto;
                });
    }

    public void deleteRoomByRoomNo(int roomNo) {
        String query = "DELETE FROM chess_room WHERE room_no = ?";
        jdbcTemplate.update(query, roomNo);
    }
}
