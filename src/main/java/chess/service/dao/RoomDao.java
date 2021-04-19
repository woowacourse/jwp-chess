package chess.service.dao;

import chess.controller.dto.RoomInfoDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoomDao {
    private static final int COLUMN_INDEX_OF_ROOM_NAME = 2;
    private static final int COLUMN_INDEX_OF_ROOM_ID = 3;
    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(final String roomName, final long roomId) {
        if (isRoomNameExist(roomName)) {
            throw new IllegalArgumentException("중복된 방 이름입니다.");
        }

        final String query = "INSERT INTO room_status (room_name, room_id) VALUES (?, ?)";
        jdbcTemplate.update(query, roomName, roomId);
    }

    private boolean isRoomNameExist(final String roomName) {
        final String query = "SELECT EXISTS (SELECT * FROM room_status WHERE room_name = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, roomName);
    }

    public void delete(final long roomId) {
        final String query = "DELETE FROM room_status WHERE room_id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public List<RoomInfoDto> load() {
        final String query = "SELECT * FROM room_status";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeRoomDto(rs));
    }

    private RoomInfoDto makeRoomDto(final ResultSet rs) throws SQLException {
        final long id = rs.getLong(COLUMN_INDEX_OF_ROOM_ID);
        final String name = rs.getString(COLUMN_INDEX_OF_ROOM_NAME);
        return new RoomInfoDto(id, name);
    }

    public String name(final long roomId) {
        final String query = "SELECT room_name FROM room_status WHERE room_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }
}
