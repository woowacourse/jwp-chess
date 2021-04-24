package chess.service.dao;

import chess.controller.dto.RoomInfoDto;
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
    private static final int COLUMN_INDEX_OF_ROOM_ID = 1;
    private static final int COLUMN_INDEX_OF_ROOM_NAME = 2;
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = new GeneratedKeyHolder();
    }

    public long save(final String roomName, final String player1) {
        if (isRoomNameExist(roomName)) {
            throw new IllegalArgumentException("중복된 방 이름입니다.");
        }

        final String query = "INSERT INTO room (room_name, player1) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(query, new String[]{"id"});
            pstmt.setString(1, roomName);
            pstmt.setString(2, player1);
            return pstmt;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private boolean isRoomNameExist(final String roomName) {
        final String query = "SELECT EXISTS (SELECT * FROM room WHERE room_name = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, roomName);
    }

    public void delete(final long roomId) {
        final String query = "DELETE FROM room WHERE id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public List<RoomInfoDto> load() {
        final String query = "SELECT * FROM room";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeRoomDto(rs));
    }

    private RoomInfoDto makeRoomDto(final ResultSet rs) throws SQLException {
        final long id = rs.getLong(COLUMN_INDEX_OF_ROOM_ID);
        final String name = rs.getString(COLUMN_INDEX_OF_ROOM_NAME);
        return new RoomInfoDto(id, name);
    }

    public String name(final long roomId) {
        final String query = "SELECT room_name FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }
}
