package chess.dao;

import chess.dto.RoomDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAO {
    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createRoom(final String name) {
        String query = "INSERT INTO room (title, black_user, white_user, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, name, 1, 2, 1); // (이름, default_black_user_id(1), default_white_user_id(2), 상태(진행중: 1 / 종료됨: 0))
    }

    public List<RoomDTO> allRooms() {
        String query = "SELECT room.id, room.title, black.nickname AS black_user, white.nickname AS white_user, room.status " +
                "FROM room JOIN user as black on black.id = room.black_user " +
                "JOIN user as white on white.id = room.white_user ORDER BY room.status DESC, room.id DESC";

        return jdbcTemplate.query(query, mapper());
    }

    private RowMapper<RoomDTO> mapper() {
        return (resultSet, rowNum) -> {
            int status = resultSet.getInt("status");
            boolean playing = (status == 1);
            return new RoomDTO(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("black_user"),
                    resultSet.getString("white_user"),
                    status,
                    playing
            );
        };
    }

    public void changeStatusEndByRoomId(final String roomId) {
        String query = "UPDATE room SET status = 0 WHERE id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public List<String> allRoomIds() {
        RowMapper<String> rowMapper = (resultSet, rowNum) -> resultSet.getString("id");

        String query = "SELECT id FROM room";
        return jdbcTemplate.query(query, rowMapper);
    }
}
