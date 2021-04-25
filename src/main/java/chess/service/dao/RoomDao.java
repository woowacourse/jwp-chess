package chess.service.dao;

import chess.controller.dto.RoomDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoomDao {
    private static final int COLUMN_INDEX_OF_ROOM_NAME = 2;
    private static final int COLUMN_INDEX_OF_ROOM_ID = 1;
    private final JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = new GeneratedKeyHolder();
    }

    public long save(final String roomName) {
        final String query = "INSERT INTO room_status (room_name) VALUES (?)";
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(query, new String[]{"id"});
            pstmt.setString(1, roomName);
            return pstmt;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void delete(final Long roomId) {
        final String query = "DELETE FROM room_status WHERE id = ?";
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
        final String query = "SELECT room_name FROM room_status WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new SQLException("해당 아이디와 일치하는 방을 찾을 수 없습니다.");
        }
    }
}
