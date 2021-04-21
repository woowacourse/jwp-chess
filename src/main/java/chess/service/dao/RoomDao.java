package chess.service.dao;

import chess.controller.dto.RoomDto;
import org.springframework.dao.EmptyResultDataAccessException;
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
        final String query = "INSERT INTO room_status (room_name, room_id) VALUES (?, ?)";
        jdbcTemplate.update(query, roomName, roomId);
    }

    public void delete(final Long roomId) {
        final String query = "DELETE FROM room_status WHERE room_id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public List<RoomDto> load() {
        final String query = "SELECT * FROM room_status";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeRoomDto(rs));
    }

    private RoomDto makeRoomDto(final ResultSet rs) throws SQLException {
        final Long id = rs.getLong(COLUMN_INDEX_OF_ROOM_ID);
        final String name = rs.getString(COLUMN_INDEX_OF_ROOM_NAME);
        return new RoomDto(id, name);
    }

    public String name(final Long roomId) throws SQLException {
        final String query = "SELECT room_name FROM room_status WHERE room_id = ?";

        try {
            return jdbcTemplate.queryForObject(query, String.class, roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new SQLException();
        }
    }
}
